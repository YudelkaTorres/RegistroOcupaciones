package edu.ucne.registroocupaciones.domain.horasExtras.repository

import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import kotlinx.coroutines.flow.Flow

interface HoraExtraRepository {

    fun observeHorasExtras(): Flow<List<HoraExtra>>

    suspend fun getHoraExtra(id: Int): HoraExtra?

    suspend fun upsert(horaExtra: HoraExtra): Int

    suspend fun delete(id: Int)

    suspend fun exists(id: Int): Boolean
}