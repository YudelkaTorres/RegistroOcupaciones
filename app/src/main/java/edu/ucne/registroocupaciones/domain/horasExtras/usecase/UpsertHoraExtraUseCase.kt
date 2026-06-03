package edu.ucne.registroocupaciones.domain.horasExtras.usecase

import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import javax.inject.Inject

class UpsertHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(horaExtra: HoraExtra): Result<Int> {
        val empleadoResult = validateEmpleado(horaExtra.empleadoId)
        if (!empleadoResult.isValid) {
            return Result.failure(IllegalArgumentException(empleadoResult.error))
        }

        val tipoResult = validateTipoHoraExtra(horaExtra.tipoHoraExtra)
        if (!tipoResult.isValid) {
            return Result.failure(
                IllegalArgumentException(tipoResult.error)
            )
        }

        val horasResult =
            validateHorasTrabajadas(horaExtra.horasTrabajadas.toString())
        if (!horasResult.isValid) {
            return Result.failure(IllegalArgumentException(horasResult.error))
        }
        return runCatching { repository.upsert(horaExtra) }
    }
}