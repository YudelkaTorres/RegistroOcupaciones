package edu.ucne.registroocupaciones.domain.ocupaciones.usecase

import edu.ucne.registroocupaciones.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion
import javax.inject.Inject

class GetOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    suspend operator fun invoke(id: Int): Ocupacion?{
        if (id <= 0) throw IllegalArgumentException("El id debe de ser mayor a 0")
        return repository.getOcupacion(id)
    }
}