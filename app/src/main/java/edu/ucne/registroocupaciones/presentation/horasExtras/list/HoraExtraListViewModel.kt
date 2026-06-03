package edu.ucne.registroocupaciones.presentation.horasExtras.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.DeleteHoraExtraUseCase
import edu.ucne.registroocupaciones.domain.horasExtras.usecase.ObserveHorasExtrasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoraExtraListViewModel @Inject constructor(
    private val observeHorasExtrasUseCase: ObserveHorasExtrasUseCase,
    private val deleteHoraExtraUseCase: DeleteHoraExtraUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HoraExtraListUiState(isLoading = true))
    val state : StateFlow<HoraExtraListUiState> = _state.asStateFlow()

    init {
        loadHorasExtras()
    }

    fun onEvent(event: HoraExtraListUiEvent) {
        when (event) {
            HoraExtraListUiEvent.Load -> loadHorasExtras()
            HoraExtraListUiEvent.Refresh -> loadHorasExtras()
            is HoraExtraListUiEvent.Delete -> onDelete(event.id)
            is HoraExtraListUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            HoraExtraListUiEvent.ClearMessage -> _state.update { it.copy(message = null) }
            HoraExtraListUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is HoraExtraListUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            HoraExtraListUiEvent.ClearNavigation -> _state.update { it.copy(navigateToEditId = null, navigateToCreate = false) }
        }
    }

    fun loadHorasExtras() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            observeHorasExtrasUseCase().collectLatest { list ->
                _state.update { it.copy(isLoading = false, horasExtras = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteHoraExtraUseCase(id)
            onEvent(HoraExtraListUiEvent.ShowMessage("Eliminado"))
        }
    }
}