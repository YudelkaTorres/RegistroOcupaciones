package edu.ucne.registroocupaciones.presentation.ocupacion.list

sealed interface OcupacionListUIEvent {
    data object Load : OcupacionListUIEvent
    data class Delete(val id: Int) : OcupacionListUIEvent
    data object CreateNew : OcupacionListUIEvent
    data class Edit(val id: Int) : OcupacionListUIEvent
    data class ShowMessage(val message: String) : OcupacionListUIEvent
}