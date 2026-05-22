package edu.ucne.registroocupaciones.presentation.ocupaciones.edit

sealed interface OcupacionEditUiEvent {
    data class Load(val id: Int?) : OcupacionEditUiEvent
    data class DescripcionChanged(val value: String) : OcupacionEditUiEvent
    data class SueldoChanged(val value: String) : OcupacionEditUiEvent
    data object Save : OcupacionEditUiEvent
    data object Delete : OcupacionEditUiEvent
}