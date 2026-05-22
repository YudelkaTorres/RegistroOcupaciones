package edu.ucne.registroocupaciones.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoEntity
import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionEntity

@Database(
    entities = [
        OcupacionEntity::class,
        EmpleadoEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class OcupacionDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun empleadoDao(): EmpleadoDao
}