package edu.ucne.registroocupaciones.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object OcupacionList : Screen()
    @Serializable
    data object  OcupacionForm : Screen()
    @Serializable
    data class OcupacionEdit(val ocupacionId: Int) : Screen()
}