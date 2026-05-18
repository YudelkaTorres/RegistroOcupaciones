package edu.ucne.registroocupaciones.presentation.ocupaciones.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.GetOcupacionUseCase
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.UpsertOcupacionUseCase
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.OcupacionValidations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcupacionEditViewModel @Inject constructor(
    private val getOcupacionUseCase: GetOcupacionUseCase,
    private val upsertOcupacionUseCase: UpsertOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase,
    private val ocupacionValidations: OcupacionValidations
) : ViewModel() {

    private val _state = MutableStateFlow(OcupacionEditUiState())
    val state: StateFlow<OcupacionEditUiState> = _state.asStateFlow()

    private fun validateDescripcion(descripcion: String) {
        viewModelScope.launch {
            val result = ocupacionValidations(
                descripcion = descripcion,
                sueldo = _state.value.sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )
            _state.update { it.copy(descripcionError = result.descripcionError) }
        }
    }

    private fun validateSueldo(sueldo: Double?) {
        viewModelScope.launch {
            val result = ocupacionValidations(
                descripcion = _state.value.descripcion,
                sueldo = sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )
            _state.update { it.copy(sueldoError = result.sueldoError) }
        }
    }

    fun onEvent(event: OcupacionEditUiEvent) {
        when (event) {
            is OcupacionEditUiEvent.Load -> onLoad(event.id)
            is OcupacionEditUiEvent.DescripcionChanged -> {
                _state.update { it.copy(descripcion = event.value, descripcionError = null) }
                validateDescripcion(event.value)
            }
            is OcupacionEditUiEvent.SueldoChanged -> {
                val sueldoDouble = event.value.toDoubleOrNull()
                _state.update { it.copy(sueldo = sueldoDouble, sueldoError = null) }
                validateSueldo(sueldoDouble)
            }
            OcupacionEditUiEvent.Save -> onSave()
            OcupacionEditUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, ocupacionId = null) }
            return
        }

        viewModelScope.launch {
            val ocupacion = getOcupacionUseCase(id)

            ocupacion?.let { item ->
                _state.update { currentState ->
                    currentState.copy(
                        isNew = false,
                        ocupacionId = item.ocupacionId,
                        descripcion = item.descripcion,
                        sueldo = item.sueldo
                    )
                }
            }
        }
    }

    private fun onSave() {
        viewModelScope.launch {
            val validation = ocupacionValidations(
                descripcion = _state.value.descripcion,
                sueldo = _state.value.sueldo,
                currentOcupacionId = _state.value.ocupacionId
            )

            if (!validation.isValid) {
                _state.update {
                    it.copy(
                        descripcionError = validation.descripcionError,
                        sueldoError = validation.sueldoError
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSaving = true) }
            try {
                val ocupacion = Ocupacion(
                    ocupacionId = _state.value.ocupacionId ?: 0,
                    descripcion = _state.value.descripcion,
                    sueldo = _state.value.sueldo ?: 0.0
                )
                upsertOcupacionUseCase(ocupacion)
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, descripcionError = e.message) }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.ocupacionId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            try {
                deleteOcupacionUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isDeleting = false, descripcionError = e.message) }
            }
        }
    }
}