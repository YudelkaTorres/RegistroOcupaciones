package edu.ucne.registroocupaciones.domain.ocupaciones.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion
import edu.ucne.registroocupaciones.domain.ocupaciones.repository.OcupacionRepository

class ObserveOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> {
        return repository.observeOcupaciones()
    }
}