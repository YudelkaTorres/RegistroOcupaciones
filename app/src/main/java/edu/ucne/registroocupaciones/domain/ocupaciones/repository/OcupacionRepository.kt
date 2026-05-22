package edu.ucne.registroocupaciones.domain.ocupaciones.repository

import kotlinx.coroutines.flow.Flow
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion

interface OcupacionRepository {
    fun observeOcupaciones(): Flow<List<Ocupacion>>
    suspend fun getOcupacion(id: Int): Ocupacion?
    suspend fun upsert(ocupacion: Ocupacion): Int
    suspend fun delete(id: Int)
    suspend fun getOcupacionesbyDescripcion(descripcion: String): List<Ocupacion>

}