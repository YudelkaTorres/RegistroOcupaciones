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

class GetEmpleadoUseCaseTest {

    private lateinit var repository: EmpleadoRepository
    private lateinit var getEmpleadoUseCase: GetEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        getEmpleadoUseCase = GetEmpleadoUseCase(repository)
    }

    @Test
    fun `retorna empleado por id`() = runTest {

        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = "01/01/2024",
            nombres = "Juan",
            sexo = "M",
            sueldo = 20000.0
        )

        coEvery { repository.getEmpleado(1) } returns empleado

        val result = getEmpleadoUseCase(1)

        assertEquals("Juan", result?.nombres)

        coVerify { repository.getEmpleado(1) }
    }
}