package edu.ucne.registroocupaciones.presentation.horasExtras.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.horasExtras.model.HoraExtra
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu

@Composable
fun HoraExtraListScreen(
    onDrawer: () -> Unit,
    viewModel: HoraExtraListViewModel = hiltViewModel(),
    onAddHoraExtra: () -> Unit,
    onNavigateToEdit: (Int) -> Unit

) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onAddHoraExtra()
            viewModel.onEvent(HoraExtraListUiEvent.ClearNavigation)
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
            viewModel.onEvent(HoraExtraListUiEvent.ClearNavigation)
        }
    }

    HoraExtraListBody(state, viewModel::onEvent, onAddHoraExtra, onDrawer)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraListBody(
    state: HoraExtraListUiState,
    onEvent: (HoraExtraListUiEvent) -> Unit,
    onAddHoraExtra: () -> Unit,
    onDrawer: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {

        state.message?.let { message ->

            snackbarHostState.showSnackbar(message)

            onEvent(HoraExtraListUiEvent.ClearMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Horas Extras") }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = onAddHoraExtra) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar hora extra"
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            if(state.isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("loading")
                )

            } else {

                if(state.horasExtras.isEmpty()) {

                    Text(
                        text = "No hay horas extras",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .testTag("empty_message"),

                        style =
                            MaterialTheme
                                .typography
                                .bodyLarge
                    )

                } else {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),

                        contentPadding =
                            PaddingValues(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.horasExtras,
                            key = { it.horaExtraId }
                        ) { horaExtra ->

                            HoraExtraItem(
                                horaExtra = horaExtra,

                                onDelete = {
                                    onEvent(
                                        HoraExtraListUiEvent.Delete(
                                            horaExtra.horaExtraId
                                        )
                                    )
                                },

                                onClick = {
                                    onEvent(
                                        HoraExtraListUiEvent.Edit(
                                            horaExtra.horaExtraId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraItem(
    horaExtra: HoraExtra,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(
                "hora_extra_item_${horaExtra.horaExtraId}"
            ),

        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "Empleado: ${horaExtra.empleadoId}",

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )

                Text(
                    text =
                        "Horas: ${horaExtra.horasTrabajadas}",

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium
                )

                Text(
                    text =
                        "Monto: ${horaExtra.montoTotal}",

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    color =
                        MaterialTheme
                            .colorScheme
                            .primary
                )
            }

            IconButton(onClick = onClick) {
                Icon(Icons.Default.Edit, "Editar")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar")
            }
        }
    }
}