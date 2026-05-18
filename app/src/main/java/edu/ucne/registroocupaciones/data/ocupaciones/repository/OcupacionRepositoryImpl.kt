package edu.ucne.registroocupaciones.data.ocupaciones.repository

import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.ocupaciones.mapper.toDomain
import edu.ucne.registroocupaciones.data.ocupaciones.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion
import edu.ucne.registroocupaciones.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Inject

class OcupacionRepositoryImpl @Inject constructor(
    private val ocupacionDao: OcupacionDao
): OcupacionRepository {

    override fun observeOcupaciones() : Flow<List<Ocupacion>> {
        return  ocupacionDao.observeAll().map { entities ->
            entities.map {it.toDomain()}
        }
    }

    override suspend fun getOcupacion(id: Int): Ocupacion? {
        return ocupacionDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(ocupacion: Ocupacion): Int {
        val entity = ocupacion.toEntity()
        val result = ocupacionDao.upsert(ocupacion = entity)
        return if (ocupacion.ocupacionId==0) result.toInt() else ocupacion.ocupacionId
    }

    override suspend fun delete (id: Int) {
        ocupacionDao.deleteBY(id)
    }

    override suspend fun getOcupacionesbyDescripcion(descripcion: String): List<Ocupacion> {
        return ocupacionDao.getOcupacionesByDescripcion(descripcion).map { it.toDomain() }
    }
}
