package edu.ucne.registroocupaciones.domain.ocupaciones.usecase

import edu.ucne.registroocupaciones.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion
import javax.inject.Inject

class UpsertOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository,
    private val ocupacionValidations: OcupacionValidations
) {
    suspend operator fun invoke(ocupacion: Ocupacion): Result<Int> {
        return try {
            val validation = ocupacionValidations(
                descripcion = ocupacion.descripcion,
                sueldo = ocupacion.sueldo,
                currentOcupacionId = if (ocupacion.ocupacionId != 0) ocupacion.ocupacionId else null
            )

            if (!validation.isValid) {
                val errorMsg = validation.descripcionError ?: validation.sueldoError ?: "Error de validación"
                Result.failure(IllegalArgumentException(errorMsg))
            } else {
                val id = repository.upsert(ocupacion)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}