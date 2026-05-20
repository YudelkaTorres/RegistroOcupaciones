package edu.ucne.registroocupaciones.domain.empleados.usecase

import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpsertEmpleadoUseCaseTest {

    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: UpsertEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertEmpleadoUseCase(repository)
    }

    @Test
    fun `upsert empleado retorna id correctamente`() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan",
            sexo = "M",
            sueldo = 20000.0,
            fechaIngreso = "01/01/2024"
        )

        coEvery { repository.upsert(empleado) } returns 1

        val result = useCase(empleado)

        assertEquals(1, result.getOrNull())

        coVerify { repository.upsert(empleado) }
    }
}