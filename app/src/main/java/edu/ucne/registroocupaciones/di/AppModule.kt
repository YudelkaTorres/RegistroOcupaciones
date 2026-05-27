package edu.ucne.registroocupaciones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupaciones.data.database.OcupacionDb
import edu.ucne.registroocupaciones.data.ocupaciones.local.dao.OcupacionDao
import edu.ucne.registroocupaciones.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registroocupaciones.data.ocupaciones.repository.OcupacionRepositoryImpl
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.empleados.repository.EmpleadoRepositoryImpl
import edu.ucne.registroocupaciones.data.horasExtras.local.dao.HoraExtraDao
import edu.ucne.registroocupaciones.data.horasExtras.repository.HoraExtraRepositoryImpl
import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.horasExtras.repository.HoraExtraRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module

object AppModule {
    @Provides
    @Singleton
    fun provideOcupacionDb(@ApplicationContext appContext: Context): OcupacionDb {
        return Room.databaseBuilder(
            appContext,
            OcupacionDb::class.java,
            "OcupacionDb"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideOcupacionDao(ocupacionDb: OcupacionDb): OcupacionDao {
        return ocupacionDb.ocupacionDao()
    }

    @Provides
    @Singleton
    fun provideOcupacionRepositoryImpl(ocupacionDao: OcupacionDao): OcupacionRepositoryImpl {
        return OcupacionRepositoryImpl(ocupacionDao)
    }

    @Provides
    @Singleton
    fun provideOcupacionRepository(impl: OcupacionRepositoryImpl): OcupacionRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideEmpleadoDao(db: OcupacionDb): EmpleadoDao {
        return db.empleadoDao()
    }

    @Provides
    @Singleton
    fun provideEmpleadoRepositoryImpl(
        empleadoDao: EmpleadoDao
    ): EmpleadoRepositoryImpl {
        return EmpleadoRepositoryImpl(empleadoDao)
    }

    @Provides
    @Singleton
    fun provideEmpleadoRepository(
        impl: EmpleadoRepositoryImpl
    ): EmpleadoRepository {
        return impl
    }

    @Provides
    @Singleton
    fun provideHoraExtraDao(db: OcupacionDb
    ): HoraExtraDao{
        return db.horaExtraDao()
    }

    @Provides
    @Singleton
    fun provideHoraExtraRepositoryImpl( horaExtraDao: HoraExtraDao
    ): HoraExtraRepositoryImpl {
        return HoraExtraRepositoryImpl(horaExtraDao)
    }

    @Provides
    @Singleton
    fun provideHoraExtraRepository(
        impl: HoraExtraRepositoryImpl
    ): HoraExtraRepository{
        return impl
    }
}