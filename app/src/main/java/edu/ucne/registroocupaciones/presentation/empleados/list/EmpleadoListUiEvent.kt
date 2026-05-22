package edu.ucne.registroocupaciones.presentation.empleados.list

sealed interface EmpleadoListUiEvent {
    data object Load : EmpleadoListUiEvent
    data object CreateNew : EmpleadoListUiEvent

    data class Delete(val id: Int) : EmpleadoListUiEvent
    data class Edit(val id: Int) : EmpleadoListUiEvent
    data class ShowMessage(val message: String) : EmpleadoListUiEvent
    data object NavigationConsumed : EmpleadoListUiEvent
}