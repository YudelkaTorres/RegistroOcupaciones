package edu.ucne.registroocupaciones.data.horasExtras.mapper

import edu.ucne.registroocupaciones.data.horasExtras.local.dao.HoraExtraEntity
import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra

fun HoraExtraEntity.toDomain(): HoraExtra = HoraExtra(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId,
    horasTrabajadas = horasTrabajadas,
    horasNocturnas = horasNocturnas,
    horas35 = horas35,
    horas100 = horas100,
    tipoHoraExtra = tipoHoraExtra,
    montoTotal = montoTotal
)

fun HoraExtra.toEntity(): HoraExtraEntity = HoraExtraEntity(
    horaExtraId = horaExtraId,
    empleadoId = empleadoId ?: 0,
    horasTrabajadas = horasTrabajadas,
    horasNocturnas = horasNocturnas,
    horas35 = horas35,
    horas100 = horas100,
    tipoHoraExtra = tipoHoraExtra,
    montoTotal = montoTotal
)