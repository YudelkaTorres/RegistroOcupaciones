package edu.ucne.registroocupaciones.presentation.horasExtras.list

import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra

data class HoraExtraListUiState(
    val isLoading: Boolean = false,
    val horasExtras: List<HoraExtra> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null,
    val error: String? = null
)
