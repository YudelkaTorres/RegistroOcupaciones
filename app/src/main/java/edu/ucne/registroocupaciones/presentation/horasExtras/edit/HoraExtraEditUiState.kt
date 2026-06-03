package edu.ucne.registroocupaciones.presentation.horasExtras.edit

import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra

data class HoraExtraEditUiState(
    val horaExtraId: Int? = null,
    val empleadoId: Int? = null,
    val horasTrabajadas: String = "",
    val horasNocturnas: String = "",
    val horas35: Double = 0.0,
    val horas100: Double = 0.0,
    val monto35: Double = 0.0,
    val monto100: Double = 0.0,
    val montoNocturno: Double = 0.0,
    val montoTotal: Double = 0.0,
    val sueldoMensual: Double = 0.0,
    val sueldoDiario: Double = 0.0,
    val sueldoHora: Double = 0.0,
    val tipoHoraExtra: TipoHoraExtra? = null,

    val empleadoError: String? = null,
    val horasError: String? = null,
    val horasNocturnasError: String? = null,
    val tipoHoraExtraError: String? = null,

    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false,
)
