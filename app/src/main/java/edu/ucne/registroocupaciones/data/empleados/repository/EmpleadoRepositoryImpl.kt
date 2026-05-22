package edu.ucne.registroocupaciones.data.empleados.repository

import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.empleados.mapper.toDomain
import edu.ucne.registroocupaciones.data.empleados.mapper.toEntity
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EmpleadoRepositoryImpl @Inject constructor(
    private val empleadoDao: EmpleadoDao
): EmpleadoRepository {

    override fun observeEmpleados() : Flow<List<Empleado>> {
        return empleadoDao.observeAll().map { entities ->
            entities.map {it.toDomain()}
        }
    }

    override suspend fun getEmpleado(id: Int): Empleado? {
        return empleadoDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(empleado: Empleado): Int {
        val entity = empleado.toEntity()
        val result = empleadoDao.upsert(empleado = entity)
        return if (empleado.empleadoId==0) result.toInt() else empleado.empleadoId
    }

    override suspend fun delete (id: Int) {
        empleadoDao.deleteBy(id)
    }

    override suspend fun getEmpleadosbyNombres(nombres: String): List<Empleado> {
        return empleadoDao.getEmpleadosByNombre(nombres).map { it.toDomain() }
    }
}