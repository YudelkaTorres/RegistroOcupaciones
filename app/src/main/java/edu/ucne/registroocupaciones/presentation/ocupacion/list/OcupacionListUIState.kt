package edu.ucne.registroocupaciones.presentation.ocupacion.list

import edu.ucne.registroocupaciones.domain.ocupacion.model.Ocupacion

data class OcupacionListUIState (
    val isLoading: Boolean = false,
    val ocupaciones: List<Ocupacion> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)