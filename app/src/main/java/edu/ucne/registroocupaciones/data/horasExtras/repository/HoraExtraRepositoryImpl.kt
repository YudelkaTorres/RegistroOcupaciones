package edu.ucne.registroocupaciones.data.horasExtras.repository

import edu.ucne.registroocupaciones.data.horasExtras.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.data.horasExtras.mapper.toDomain
import edu.ucne.registroocupaciones.data.horasExtras.mapper.toEntity
import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HoraExtraRepositoryImpl @Inject constructor(
    private val localDataSource: HoraExtraDao
): HoraExtraRepository {
    override fun observeHorasExtras(): Flow<List<HoraExtra>> {
        return localDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHoraExtra(id: Int): HoraExtra? {
        return localDataSource.getById(id)?.toDomain()
    }

    override suspend fun upsert(horaExtra: HoraExtra): Int {
        localDataSource.upsert(horaExtra.toEntity())
        return horaExtra.horaExtraId ?: 0
    }

    override suspend fun delete(id: Int) {
        localDataSource.deleteById(id)
    }

    override suspend fun exists(id: Int): Boolean {
        return localDataSource.exists(id)
    }
}