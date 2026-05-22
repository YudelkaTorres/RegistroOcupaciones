package edu.ucne.registroocupaciones.domain.empleados.usecase

import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObserveEmpleadoUseCaseTest {
    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: ObserveEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ObserveEmpleadoUseCase(repository)
    }

    @Test
    fun `observe empleados retorna flow correctamente`() = runTest {

        val empleados = listOf(
            Empleado(1,"01/01/2024","Juan","M",20000.0),
            Empleado(2,"01/01/2024","Maria","F",25000.0)
        )

        every { repository.observeEmpleados() } returns flowOf(empleados)

        val result = useCase().first()

        assertEquals(2, result.size)
        assertEquals("Juan", result[0].nombres)
        assertEquals("Maria", result[1].nombres)

        verify { repository.observeEmpleados() }
    }
}