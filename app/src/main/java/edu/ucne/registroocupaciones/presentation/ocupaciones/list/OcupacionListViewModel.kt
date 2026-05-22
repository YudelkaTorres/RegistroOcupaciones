package edu.ucne.registroocupaciones.presentation.ocupaciones.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.DeleteOcupacionUseCase
import edu.ucne.registroocupaciones.domain.ocupaciones.usecase.ObserveOcupacionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OcupacionListViewModel @Inject constructor(
    private val observeOcupacionUseCase: ObserveOcupacionUseCase,
    private val deleteOcupacionUseCase: DeleteOcupacionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(OcupacionListUiState(isLoading = true))
    val state: StateFlow<OcupacionListUiState> = _state.asStateFlow()

    init {
        onEvent(OcupacionListUiEvent.Load)
    }

    fun onEvent(event: OcupacionListUiEvent) {
        when (event) {
            OcupacionListUiEvent.Load -> observeOcupaciones()
            is OcupacionListUiEvent.Delete -> onDelete(event.id)
            OcupacionListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is OcupacionListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is OcupacionListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observeOcupaciones() {
        viewModelScope.launch {
            observeOcupacionUseCase().collectLatest { ocupacionesList ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        ocupaciones = ocupacionesList,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            try {
                deleteOcupacionUseCase(id)
                onEvent(OcupacionListUiEvent.ShowMessage("Ocupación eliminada"))
            } catch (e: Exception) {
                onEvent(OcupacionListUiEvent.ShowMessage("Error al eliminar: ${e.message}"))
            }
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null,
                message = null
            )
        }
    }
}