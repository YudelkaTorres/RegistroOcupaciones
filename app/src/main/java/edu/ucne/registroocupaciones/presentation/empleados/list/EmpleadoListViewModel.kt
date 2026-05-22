package edu.ucne.registroocupaciones.presentation.empleados.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.empleados.usecase.DeleteEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.empleados.usecase.ObserveEmpleadoUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoListViewModel @Inject constructor(
    private val observeEmpleadosUseCase: ObserveEmpleadoUseCase,
    private val deleteEmpleadoUseCase: DeleteEmpleadoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmpleadoListUiState())
    val state: StateFlow<EmpleadoListUiState> = _state.asStateFlow()

    init {
        loadEmpleados()
    }

    fun onEvent(event: EmpleadoListUiEvent) {
        when (event) {

            EmpleadoListUiEvent.Load -> loadEmpleados()

            is EmpleadoListUiEvent.Delete -> onDelete(event.id)

            is EmpleadoListUiEvent.ShowMessage ->
                _state.update { it.copy(errorMessage = event.message) }

            EmpleadoListUiEvent.CreateNew ->
                _state.update { it.copy(navigateToCreate = true) }

            is EmpleadoListUiEvent.Edit ->
                _state.update { it.copy(navigateToEditId = event.id) }

            EmpleadoListUiEvent.NavigationConsumed ->
                _state.update {
                    it.copy(
                        navigateToCreate = false,
                        navigateToEditId = null
                    )
                }
        }
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            observeEmpleadosUseCase().collectLatest { lista ->
                _state.update {
                    it.copy(
                        empleados = lista,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteEmpleadoUseCase(id)
        }
    }
}