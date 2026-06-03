package edu.ucne.registroocupaciones.data.horasExtras.local.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra

@Entity(tableName = "horasExtras")
data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraId: Int = 0,
    val empleadoId: Int = 0,
    val horasTrabajadas: Double,
    val horasNocturnas: Double,
    val horas35: Double,
    val horas100: Double,
    val tipoHoraExtra: TipoHoraExtra,
    val montoTotal: Double

)
