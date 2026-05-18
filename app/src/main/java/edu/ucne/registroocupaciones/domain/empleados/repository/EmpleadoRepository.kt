package edu.ucne.registroocupaciones.domain.empleados.repository

import kotlinx.coroutines.flow.Flow
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado

interface EmpleadoRepository {
    fun observeEmpleados(): Flow<List<Empleado>>
    suspend fun getEmpleado(id: Int): Empleado?
    suspend fun upsert(empleado: Empleado): Int
    suspend fun delete(id: Int)

    suspend fun getEmpleadosbyNombres(nombres: String): List<Empleado>
}