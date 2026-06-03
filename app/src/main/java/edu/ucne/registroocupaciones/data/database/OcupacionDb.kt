package edu.ucne.registroocupaciones.data.database

import androidx.room.TypeConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoEntity
import edu.ucne.registroocupaciones.data.horasExtras.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.data.horasExtras.local.dao.HoraExtraEntity
import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionEntity

@Database(
    entities = [
        OcupacionEntity::class,
        EmpleadoEntity::class,
        HoraExtraEntity::class
    ],
    version = 5,
    exportSchema = false
)

@TypeConverters(TipoHoraExtraConverter::class)

abstract class OcupacionDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun empleadoDao(): EmpleadoDao

    abstract fun horaExtraDao(): HoraExtraDao
}