package edu.ucne.registroocupaciones.domain.horasExtras.model
data class HoraExtra(
    val horaExtraId: Int = 0,
    val empleadoId: Int? = 0,
    val horasTrabajadas: Double,
    val horasNocturnas: Double,
    val horas35: Double,
    val horas100: Double,
    val tipoHoraExtra: TipoHoraExtra,
    val montoTotal: Double
)
