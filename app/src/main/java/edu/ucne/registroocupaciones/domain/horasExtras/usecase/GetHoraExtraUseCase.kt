package edu.ucne.registroocupaciones.domain.horasExtras.usecase

import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import javax.inject.Inject

class GetHoraExtraUseCase @Inject constructor(
    private val repository: HoraExtraRepository
) {
    suspend operator fun invoke(id: Int): HoraExtra? = repository.getHoraExtra(id)
}