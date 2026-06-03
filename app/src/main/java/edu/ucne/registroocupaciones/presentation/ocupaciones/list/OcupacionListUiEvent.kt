package edu.ucne.registroocupaciones.presentation.ocupaciones.list

sealed interface OcupacionListUiEvent {
    data object Load : OcupacionListUiEvent
    data class Delete(val id: Int) : OcupacionListUiEvent
    data object CreateNew : OcupacionListUiEvent
    data class Edit(val id: Int) : OcupacionListUiEvent
    data class ShowMessage(val message: String) : OcupacionListUiEvent

    object ClearNavigation : OcupacionListUiEvent
}