package edu.ucne.registroocupaciones.data.empleados.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoDao
import edu.ucne.registroocupaciones.data.empleados.local.dao.EmpleadoEntity
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EmpleadoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: EmpleadoRepositoryImpl
    private lateinit var dao: EmpleadoDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda empleado correctamente`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan Perez",
            sexo = "M",
            sueldo = 20000.0,
            fechaIngreso = "01/01/2024"
        )

        val slot = slot<EmpleadoEntity>()

        coEvery { dao.upsert(capture(slot)) } returns 1

        val result = repository.upsert(empleado)

        assertEquals(1, result)
        coVerify { dao.upsert(any()) }

        assertEquals(empleado.nombres, slot.captured.nombres)
        assertEquals(empleado.sexo, slot.captured.sexo)
    }

    @Test
    fun `upsert actualiza empleado correctamente`() = runTest {

        val empleado = Empleado(
            empleadoId = 1,
            nombres = "Empleado Editado",
            sexo = "F",
            sueldo = 25000.0,
            fechaIngreso = "01/01/2024"
        )

        coEvery { dao.upsert(any()) } returns 1

        val result = repository.upsert(empleado)

        assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina empleado correctamente`() = runTest {

        val empleadoId = 1

        coEvery { dao.deleteBy(empleadoId) } just Runs

        repository.delete(empleadoId)

        coVerify { dao.deleteBy(empleadoId) }
    }

    @Test
    fun `observeEmpleados retorna flow correctamente`() = runTest {

        val entities = listOf(
            EmpleadoEntity(
                empleadoId = 1,
                fechaIngreso = "01/01/2024",
                nombres = "Juan",
                sexo = "M",
                sueldo = 20000.0
            ),
            EmpleadoEntity(
                empleadoId = 2,
                fechaIngreso = "01/01/2024",
                nombres = "Maria",
                sexo = "F",
                sueldo = 25000.0
            )
        )

        every { dao.observeAll() } returns flowOf(entities)

        val result = repository.observeEmpleados().first()

        assertEquals(2, result.size)
        assertEquals("Juan", result[0].nombres)
        assertEquals("Maria", result[1].nombres)
    }

    @Test
    fun `getEmpleado retorna empleado por id`() = runTest {

        val entity = EmpleadoEntity(
            empleadoId = 1,
            fechaIngreso = "01/01/2024",
            nombres = "Carlos",
            sexo = "M",
            sueldo = 30000.0
        )

        coEvery { dao.getById(1) } returns entity

        val result = repository.getEmpleado(1)

        assertNotNull(result)
        assertEquals("Carlos", result?.nombres)
        assertEquals(30000.0, result?.sueldo)
    }
}