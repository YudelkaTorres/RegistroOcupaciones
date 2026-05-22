package edu.ucne.registroocupaciones.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object OcupacionList : Screen()
    @Serializable
    data class OcupacionEdit(val ocupacionId: Int) : Screen()
    @Serializable
    data object EmpleadoList : Screen()

    @Serializable
    data class EmpleadoEdit(val empleadoId: Int) : Screen()
}