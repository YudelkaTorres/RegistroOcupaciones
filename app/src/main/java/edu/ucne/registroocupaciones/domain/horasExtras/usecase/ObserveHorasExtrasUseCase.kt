package edu.ucne.registroocupaciones.domain.horasExtras.usecase

import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHorasExtrasUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    operator fun invoke(): Flow<List<HoraExtra>> = repository.observeHorasExtras()
}