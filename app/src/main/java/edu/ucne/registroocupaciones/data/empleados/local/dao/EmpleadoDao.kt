package edu.ucne.registroocupaciones.data.empleados.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Query(value = "SELECT * FROM empleados ORDER BY empleadoId DESC")
    fun observeAll(): Flow<List<EmpleadoEntity>>

    @Query(value = "SELECT *FROM empleados WHERE empleadoId = :id")
    fun getById(id: Int): EmpleadoEntity?

    @Upsert
    suspend fun upsert(empleado: EmpleadoEntity) : Long

    @Delete
    suspend fun delete(empleado: EmpleadoEntity)

    @Query(value = "DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun deleteBy(id: Int)

    @Query(value = "SELECT * FROM empleados WHERE nombres = :nombres")
    suspend fun getEmpleadosByNombre(nombres: String): List<EmpleadoEntity>
}