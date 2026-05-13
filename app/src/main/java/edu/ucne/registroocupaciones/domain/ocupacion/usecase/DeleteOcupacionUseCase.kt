package edu.ucne.registroocupaciones.domain.ocupacion.usecase

import javax.inject.Inject
import edu.ucne.registroocupaciones.domain.ocupacion.repository.OcupacionRepository

class DeleteOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int) {
        if (id <= 0) throw IllegalArgumentException("El ID debe ser mayor que 0")
        repository.delete(id)
    }
}