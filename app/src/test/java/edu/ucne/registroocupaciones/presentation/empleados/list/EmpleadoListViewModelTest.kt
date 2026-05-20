package edu.ucne.registroocupaciones.presentation.empleados.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import edu.ucne.registroocupaciones.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.ObserveEmpleadoUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@ExperimentalCoroutinesApi
class EmpleadoListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: EmpleadoListViewModel
    private lateinit var observeEmpleadoUseCase: ObserveEmpleadoUseCase
    private lateinit var deleteEmpleadoUseCase: DeleteEmpleadoUseCase

    @Before
    fun setup() {
        observeEmpleadoUseCase = mockk()
        deleteEmpleadoUseCase = mockk()

        every { observeEmpleadoUseCase() } returns flowOf(emptyList())

        viewModel = EmpleadoListViewModel(
            observeEmpleadoUseCase,
            deleteEmpleadoUseCase
        )
    }

    @Test
    fun loadEmpleados_carga_lista_correctamente() = runTest {

        val empleados = listOf(
            Empleado(1, "01/01/2024", "Juan Perez", "Masculino", 25000.0),
            Empleado(2, "01/01/2024", "Maria Lopez", "Femenino", 30000.0)
        )

        every { observeEmpleadoUseCase() } returns flowOf(empleados)

        viewModel = EmpleadoListViewModel(
            observeEmpleadoUseCase,
            deleteEmpleadoUseCase
        )

        assertEquals(2, viewModel.state.value.empleados.size)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun delete_elimina_empleado_correctamente() = runTest {

        val empleadoId = 1
        coEvery { deleteEmpleadoUseCase(empleadoId) } just Runs

        viewModel.onEvent(EmpleadoListUiEvent.Delete(empleadoId))

        coVerify { deleteEmpleadoUseCase(empleadoId) }
        assertTrue(viewModel.state.value.navigateToCreate == false)
    }

    @Test
    fun create_new_activa_navegacion() {

        viewModel.onEvent(EmpleadoListUiEvent.CreateNew)

        assertTrue(viewModel.state.value.navigateToCreate)
    }

    @Test
    fun edit_guarda_id_correctamente() {

        val id = 5

        viewModel.onEvent(EmpleadoListUiEvent.Edit(id))

        assertEquals(id, viewModel.state.value.navigateToEditId)
    }
}

// Regla para el Dispatcher Main en tests
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}