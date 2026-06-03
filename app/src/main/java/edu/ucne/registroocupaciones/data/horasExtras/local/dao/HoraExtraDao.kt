package edu.ucne.registroocupaciones.data.horasExtras.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraExtraDao {
    @Upsert
    suspend fun upsert(entity: HoraExtraEntity)

    @Delete
    suspend fun delete(entity: HoraExtraEntity)

    @Query("SELECT * FROM horasExtras ORDER BY horaExtraId DESC")
    fun observeAll(): Flow<List<HoraExtraEntity>>

    @Query("SELECT * FROM horasExtras WHERE horaExtraId = :id")
    suspend fun getById(id: Int): HoraExtraEntity?

    @Query("DELETE FROM horasExtras WHERE horaExtraId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM horasExtras WHERE horaExtraId = :id)")
    suspend fun exists(id: Int): Boolean
}