package edu.ucne.registroocupaciones.domain.ocupacion.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import edu.ucne.registroocupaciones.domain.ocupacion.model.Ocupacion
import edu.ucne.registroocupaciones.domain.ocupacion.repository.OcupacionRepository

class ObserveOcupacionUseCase @Inject constructor(
    private val repository: OcupacionRepository
) {
    operator fun invoke(): Flow<List<Ocupacion>> {
        return repository.observeOcupaciones()
    }
}