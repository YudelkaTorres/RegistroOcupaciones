package edu.ucne.registroocupaciones.presentation.horasExtras.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.ObserveEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.DeleteHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.GetHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.UpsertHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.validateEmpleado
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.validateHorasNocturnas
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.validateHorasTrabajadas
import edu.ucne.registroocupaciones.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

@HiltViewModel
class HoraExtraEditViewModel @Inject constructor(
    private val getHoraExtraUseCase: GetHoraExtraUseCase,
    private val upsertHoraExtraUseCase: UpsertHoraExtraUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase,
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val observeEmpleadoUseCase: ObserveEmpleadoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val routeArgs = savedStateHandle.toRoute<Screen.HoraExtraEdit>()

    private val horaExtraId: Int =
        routeArgs.horaExtraId

    private val _state =
        MutableStateFlow(HoraExtraEditUiState())

    val state: StateFlow<HoraExtraEditUiState> =
        _state.asStateFlow()

    private val _empleados =
        MutableStateFlow<List<Empleado>>(emptyList())

    val empleados =
        _empleados.asStateFlow()

    init {
        loadEmpleados()
        loadHoraExtra(horaExtraId)
    }

    fun onEvent(event: HoraExtraEditUiEvent) {

        when (event) {

            is HoraExtraEditUiEvent.Load -> {
                loadHoraExtra(event.id)
            }

            is HoraExtraEditUiEvent.EmpleadoChanged -> {

                _state.update {
                    it.copy(
                        empleadoId = event.empleadoId,
                        empleadoError = null
                    )
                }

                recalcular()
            }

            is HoraExtraEditUiEvent.HorasTrabajadasChanged -> {

                _state.update {
                    it.copy(
                        horasTrabajadas = event.value,
                        horasError = null
                    )
                }

                recalcular()
            }

            is HoraExtraEditUiEvent.HorasNocturnasChanged -> {

                _state.update {
                    it.copy(
                        horasNocturnas = event.value,
                        horasNocturnasError = null
                    )
                }

                recalcular()
            }

            is HoraExtraEditUiEvent.TipoHoraExtraChanged -> {

                _state.update {
                    it.copy(
                        tipoHoraExtra = event.value,
                        tipoHoraExtraError = null
                    )
                }

                recalcular()
            }

            HoraExtraEditUiEvent.Save -> {
                onSave()
            }

            HoraExtraEditUiEvent.Delete -> {
                onDelete()
            }
        }
    }

    private fun loadHoraExtra(id: Int?) {

        if (id == null || id == 0) {

            _state.update {
                it.copy(
                    isNew = true,
                    horaExtraId = null
                )
            }

            return
        }

        viewModelScope.launch {

            val horaExtra =
                getHoraExtraUseCase(id)

            if (horaExtra != null) {

                _state.update {

                    it.copy(
                        isNew = false,
                        horaExtraId = horaExtra.horaExtraId,
                        empleadoId = horaExtra.empleadoId,
                        horasTrabajadas =
                            horaExtra.horasTrabajadas.toString(),
                        horasNocturnas =
                            horaExtra.horasNocturnas.toString(),
                        horas35 = horaExtra.horas35,
                        horas100 = horaExtra.horas100,
                        montoTotal = horaExtra.montoTotal,
                        tipoHoraExtra =
                            horaExtra.tipoHoraExtra
                    )
                }

                recalcular()

            } else {

                _state.update {
                    it.copy(isNew = true)
                }
            }
        }
    }

    private fun loadEmpleados() {

        viewModelScope.launch {

            observeEmpleadoUseCase()
                .collect { lista ->

                    _empleados.value = lista
                }
        }
    }

    private fun recalcular() {

        viewModelScope.launch {

            val empleadoId =
                state.value.empleadoId ?: return@launch

            val empleado =
                getEmpleadoUseCase(empleadoId)

            if (empleado == null)
                return@launch

            val sueldoMensual =
                empleado.sueldo

            val sueldoDiario =
                sueldoMensual / 23.83

            val sueldoHora =
                sueldoDiario / 8

            val horasTrabajadas =
                state.value.horasTrabajadas
                    .toDoubleOrNull() ?: 0.0

            val horasNocturnas =
                state.value.horasNocturnas
                    .toDoubleOrNull() ?: 0.0

            var horas35 = 0.0
            var horas100 = 0.0

            when (state.value.tipoHoraExtra ?: return@launch) {

                TipoHoraExtra.JORNADA_ORDINARIA -> {

                    horas35 =
                        min(
                            max(
                                horasTrabajadas - 44,
                                0.0
                            ),
                            24.0
                        )

                    horas100 =
                        max(
                            horasTrabajadas - 68,
                            0.0
                        )
                }

                TipoHoraExtra.DESCANSO_FERIADO -> {

                    horas35 = 0.0

                    horas100 =
                        horasTrabajadas
                }
            }

            val monto35 =
                horas35 *
                        sueldoHora *
                        1.35

            val monto100 =
                horas100 *
                        sueldoHora *
                        2.0

            val montoNocturno =
                horasNocturnas *
                        sueldoHora *
                        0.15

            val montoTotal =
                monto35 +
                        monto100 +
                        montoNocturno

            _state.update {

                it.copy(

                    sueldoMensual =
                        sueldoMensual.roundTo4(),

                    sueldoDiario =
                        sueldoDiario.roundTo4(),

                    sueldoHora =
                        sueldoHora.roundTo4(),

                    horas35 =
                        horas35.roundTo4(),

                    horas100 =
                        horas100.roundTo4(),

                    monto35 =
                        monto35.roundTo4(),

                    monto100 =
                        monto100.roundTo4(),

                    montoNocturno =
                        montoNocturno.roundTo4(),

                    montoTotal =
                        montoTotal.roundTo4()
                )
            }
        }
    }

    private fun Double.roundTo4(): Double {
        return round(this * 10000) / 10000
    }

    private fun onSave() {

        val empleadoValidation =
            validateEmpleado(
                state.value.empleadoId
            )

        val horasValidation =
            validateHorasTrabajadas(
                state.value.horasTrabajadas
            )

        val nocturnasValidation =
            validateHorasNocturnas(
                state.value.horasNocturnas,
                state.value.horasTrabajadas
            )

        val tipoValidation =
            validateTipoHoraExtra(state.value.tipoHoraExtra)

        if (
            !empleadoValidation.isValid ||
            !horasValidation.isValid ||
            !nocturnasValidation.isValid ||
            !tipoValidation.isValid
        ) {

            _state.update {

                it.copy(

                    empleadoError =
                        empleadoValidation.error,

                    horasError =
                        horasValidation.error,

                    horasNocturnasError =
                        nocturnasValidation.error,

                    tipoHoraExtraError =
                        tipoValidation.error
                )
            }

            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(isSaving = true)
            }

            val horaExtra = HoraExtra(

                horaExtraId =
                    state.value.horaExtraId ?: 0,

                empleadoId =
                    state.value.empleadoId ?: 0,

                horasTrabajadas =
                    state.value.horasTrabajadas
                        .toDouble(),

                horasNocturnas =
                    state.value.horasNocturnas
                        .toDoubleOrNull() ?: 0.0,

                horas35 =
                    state.value.horas35,

                horas100 =
                    state.value.horas100,

                montoTotal =
                    state.value.montoTotal,

                tipoHoraExtra =
                    state.value.tipoHoraExtra ?: TipoHoraExtra.JORNADA_ORDINARIA
            )

            val result =
                upsertHoraExtraUseCase(horaExtra)

            result.onSuccess {

                _state.update {

                    it.copy(
                        isSaving = false,
                        saved = true,
                        isNew = false
                    )
                }

            }.onFailure {

                _state.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }

    private fun validateTipoHoraExtra(tipo: TipoHoraExtra?): edu.ucne.registroocupaciones.domain.horasExtras.usecase.ValidationResult {
        return when {
            tipo == null -> edu.ucne.registroocupaciones.domain.horasExtras.usecase.ValidationResult(false, "Debe seleccionar un tipo de hora extra")
            else -> edu.ucne.registroocupaciones.domain.horasExtras.usecase.ValidationResult(true)
        }
    }

    private fun onDelete() {

        val id =
            state.value.horaExtraId ?: return

        viewModelScope.launch {

            _state.update {
                it.copy(isDeleting = true)
            }

            deleteHoraExtraUseCase(id)

            _state.update {

                it.copy(
                    isDeleting = false,
                    deleted = true
                )
            }
        }
    }
}