package edu.ucne.registroocupaciones.presentation.empleados.edit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.UpsertEmpleadoUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class EmpleadoEditViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EmpleadoEditViewModel

    private val getEmpleadoUseCase: GetEmpleadoUseCase = mockk()
    private val upsertEmpleadoUseCase: UpsertEmpleadoUseCase = mockk()
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase = mockk()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        viewModel = EmpleadoEditViewModel(
            getEmpleadoUseCase,
            upsertEmpleadoUseCase,
            deleteEmpleadoUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadEmpleado_carga_datos_correctamente() = runTest {

        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = "01/01/2024",
            nombres = "Juan",
            sexo = "M",
            sueldo = 20000.0
        )

        coEvery { getEmpleadoUseCase(1) } returns empleado

        viewModel.onEvent(
            EmpleadoEditUiEvent.LoadEmpleado(1)
        )

        advanceUntilIdle()

        val state = viewModel.state.value

        assertEquals("Juan", state.nombres)
        assertEquals("M", state.sexo)
        assertEquals("20000.0", state.sueldo)
    }

    @Test
    fun nombresChanged_actualiza_state() {

        viewModel.onEvent(
            EmpleadoEditUiEvent.NombresChanged("Maria")
        )

        assertEquals("Maria", viewModel.state.value.nombres)
    }

    @Test
    fun save_guarda_empleado_correctamente() = runTest {

        coEvery {
            upsertEmpleadoUseCase(any())
        } returns Result.success(1)

        viewModel.onEvent(
            EmpleadoEditUiEvent.NombresChanged("Pedro")
        )

        viewModel.onEvent(
            EmpleadoEditUiEvent.SexoChanged("M")
        )

        viewModel.onEvent(
            EmpleadoEditUiEvent.SueldoChanged("25000")
        )

        viewModel.onEvent(
            EmpleadoEditUiEvent.FechaIngresoSelected("01/01/2024")
        )

        viewModel.onEvent(EmpleadoEditUiEvent.Save)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state.saved)

        coVerify { upsertEmpleadoUseCase(any()) }
    }

    @Test
    fun save_muestra_errores_si_datos_invalidos() = runTest {

        viewModel.onEvent(EmpleadoEditUiEvent.Save)

        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state.nombresError != null)
        assertTrue(state.sexoError != null)
        assertTrue(state.sueldoError != null)
    }

    @Test
    fun delete_elimina_empleado_correctamente() = runTest {

        val empleado = Empleado(
            empleadoId = 1,
            nombres = "Juan",
            sexo = "M",
            sueldo = 1000.0,
            fechaIngreso = "01/01/2024"
        )

        coEvery { getEmpleadoUseCase(1) } returns empleado
        coEvery { deleteEmpleadoUseCase(1) } just Runs

        viewModel.onEvent(
            EmpleadoEditUiEvent.LoadEmpleado(1)
        )

        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(
            EmpleadoEditUiEvent.Delete
        )

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.state.value.deleted)
        assertFalse(viewModel.state.value.isDeleting)

        coVerify { deleteEmpleadoUseCase(1) }
    }
}