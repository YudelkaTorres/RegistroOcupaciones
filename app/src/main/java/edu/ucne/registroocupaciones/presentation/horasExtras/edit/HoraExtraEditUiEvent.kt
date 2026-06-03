package edu.ucne.registroocupaciones.presentation.horasExtras.edit

import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra

sealed interface HoraExtraEditUiEvent {

    data class Load(val id: Int?) : HoraExtraEditUiEvent

    data class EmpleadoChanged(val empleadoId: Int) : HoraExtraEditUiEvent

    data class HorasTrabajadasChanged(val value: String) : HoraExtraEditUiEvent

    data class  HorasNocturnasChanged(val value: String) : HoraExtraEditUiEvent

    data class TipoHoraExtraChanged(val value: TipoHoraExtra) : HoraExtraEditUiEvent

    data object Save : HoraExtraEditUiEvent

    data object Delete : HoraExtraEditUiEvent
}