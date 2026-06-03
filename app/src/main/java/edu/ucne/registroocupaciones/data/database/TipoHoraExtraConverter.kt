package edu.ucne.registroocupaciones.data.database

import androidx.room.TypeConverter
import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra

class TipoHoraExtraConverter {

    @TypeConverter
    fun fromTipoHoraExtra(value: TipoHoraExtra): String {
        return value.name
    }

    @TypeConverter
    fun toTipoHoraExtra(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }
}