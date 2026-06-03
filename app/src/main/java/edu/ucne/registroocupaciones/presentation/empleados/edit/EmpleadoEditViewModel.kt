package edu.ucne.registroocupaciones.presentation.empleados.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.EmpleadoValidations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoEditViewModel @Inject constructor(
    private val getEmpleadoUseCase: GetEmpleadoUseCase,
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoEditUiState())
    val state: StateFlow<EmpleadoEditUiState> = _state.asStateFlow()

    fun onEvent(event: EmpleadoEditUiEvent) {
        when (event) {

            is EmpleadoEditUiEvent.LoadEmpleado ->
                onLoad(event.id)

            is EmpleadoEditUiEvent.NombresChanged ->
                _state.update {
                    it.copy(
                        nombres = event.value,
                        nombresError = null
                    )
                }

            is EmpleadoEditUiEvent.SexoChanged ->
                _state.update {
                    it.copy(
                        sexo = event.value,
                        sexoError = null
                    )
                }

            is EmpleadoEditUiEvent.SueldoChanged ->
                _state.update {
                    it.copy(
                        sueldo = event.value,
                        sueldoError = null
                    )
                }

            EmpleadoEditUiEvent.ShowDatePicker ->
                _state.update { it.copy(showDatePicker = true) }

            EmpleadoEditUiEvent.HideDatePicker ->
                _state.update { it.copy(showDatePicker = false) }

            EmpleadoEditUiEvent.FechaIngresoClicked ->
                _state.update { it.copy(showDatePicker = true) }

            is EmpleadoEditUiEvent.FechaIngresoSelected ->
                _state.update {
                    it.copy(
                        fechaIngreso = event.fecha,
                        fechaIngresoError = null,
                        showDatePicker = false
                    )
                }

            EmpleadoEditUiEvent.Save -> onSave()

            EmpleadoEditUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {

        if (id == null || id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    empleadoId = null,
                    nombres = "",
                    sexo = "",
                    sueldo = "",
                    fechaIngreso = "",
                    saved = false,
                    deleted = false
                )
            }
            return
        }

        viewModelScope.launch {

            val empleado = getEmpleadoUseCase(id)

            if (empleado != null) {
                _state.update { state ->
                    state.copy(
                        empleadoId = empleado.empleadoId,
                        fechaIngreso = empleado.fechaIngreso,
                        nombres = empleado.nombres,
                        sexo = empleado.sexo,
                        sueldo = empleado.sueldo.toString(),
                        isNew = false,
                        saved = false,
                        deleted = false
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isNew = true,
                        saved = false,
                        deleted = false
                    )
                }
            }
        }
    }

    private fun onSave() {

        viewModelScope.launch {

            val current = state.value

            val sueldoDouble = current.sueldo.toDoubleOrNull()

            val fechaVal =
                EmpleadoValidations.validateFechaIngreso(current.fechaIngreso)

            val nombresVal =
                EmpleadoValidations.validateNombres(current.nombres)

            val sexoVal =
                EmpleadoValidations.validateSexo(current.sexo)

            val sueldoVal =
                EmpleadoValidations.validateSueldo(sueldoDouble)

            val isValid =
                fechaVal.isValid &&
                        nombresVal.isValid &&
                        sexoVal.isValid &&
                        sueldoVal.isValid

            if (!isValid) {
                _state.update {
                    it.copy(
                        fechaIngresoError = fechaVal.error,
                        nombresError = nombresVal.error,
                        sexoError = sexoVal.error,
                        sueldoError = sueldoVal.error
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }

            val empleado = Empleado(
                empleadoId = current.empleadoId ?: 0,
                fechaIngreso = current.fechaIngreso,
                nombres = current.nombres,
                sexo = current.sexo,
                sueldo = sueldoDouble!!
            )

            val result = upsertEmpleadoUseCase(empleado)

            result.onSuccess { id ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        saved = true,
                        empleadoId = id
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }

    private fun onDelete() {

        viewModelScope.launch {
            state.value.empleadoId?.let { id ->
                deleteEmpleadoUseCase(id)
                _state.update { it.copy(deleted = true) }
            }
        }
    }
}