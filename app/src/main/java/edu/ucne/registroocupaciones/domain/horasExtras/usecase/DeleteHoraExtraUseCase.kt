package edu.ucne.registroocupaciones.domain.horasExtras.usecase

import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import javax.inject.Inject

class DeleteHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(id: Int) = repository.delete(id)
}