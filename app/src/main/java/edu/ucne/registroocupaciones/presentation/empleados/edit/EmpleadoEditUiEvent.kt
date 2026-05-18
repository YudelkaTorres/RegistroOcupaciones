package edu.ucne.registroocupaciones.presentation.empleados.edit

sealed interface EmpleadoEditUiEvent {
    data class LoadEmpleado(val id: Int?) : EmpleadoEditUiEvent
    data class FechaIngresoSelected(val fecha: String) : EmpleadoEditUiEvent
    data class NombresChanged(val value: String) : EmpleadoEditUiEvent
    data class SexoChanged(val value: String) : EmpleadoEditUiEvent
    data class SueldoChanged(val value: String) : EmpleadoEditUiEvent
    data object ShowDatePicker : EmpleadoEditUiEvent
    data object HideDatePicker : EmpleadoEditUiEvent
    data object FechaIngresoClicked : EmpleadoEditUiEvent
    data object Save : EmpleadoEditUiEvent
    data object Delete : EmpleadoEditUiEvent
}