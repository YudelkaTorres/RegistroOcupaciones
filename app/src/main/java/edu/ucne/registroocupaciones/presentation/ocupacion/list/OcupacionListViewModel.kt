package edu.ucne.registroocupaciones.presentation.ocupacion.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.ocupacion.usecase.DeleteOcupacionUseCase
import edu.ucne.registroocupaciones.domain.ocupacion.usecase.ObserveOcupacionUseCase
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
    private val _state = MutableStateFlow(OcupacionListUIState(isLoading = true))
    val state: StateFlow<OcupacionListUIState> = _state.asStateFlow()

    init {
        onEvent(OcupacionListUIEvent.Load)
    }

    fun onEvent(event: OcupacionListUIEvent) {
        when (event) {
            OcupacionListUIEvent.Load -> observeOcupaciones()
            is OcupacionListUIEvent.Delete -> onDelete(event.id)
            OcupacionListUIEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is OcupacionListUIEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is OcupacionListUIEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
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
                onEvent(OcupacionListUIEvent.ShowMessage("Ocupación eliminada"))
            } catch (e: Exception) {
                onEvent(OcupacionListUIEvent.ShowMessage("Error al eliminar: ${e.message}"))
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