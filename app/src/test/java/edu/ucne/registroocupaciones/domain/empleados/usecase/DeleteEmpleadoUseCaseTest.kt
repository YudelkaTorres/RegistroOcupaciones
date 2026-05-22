package edu.ucne.registroocupaciones.domain.empleados.usecase

import edu.ucne.registroocupaciones.domain.empleados.repository.EmpleadoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteEmpleadoUseCaseTest {
    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: DeleteEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = DeleteEmpleadoUseCase(repository)
    }

    @Test
    fun `delete empleado llama repository`() = runTest {

        coEvery { repository.delete(1) } returns Unit

        useCase(1)

        coVerify { repository.delete(1) }
    }
}