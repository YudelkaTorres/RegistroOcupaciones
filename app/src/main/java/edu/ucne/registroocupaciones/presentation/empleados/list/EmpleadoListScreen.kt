package edu.ucne.registroocupaciones.presentation.empleados.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.empleados.model.Empleado
import androidx.compose.material.icons.filled.Edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoListScreen(
    onDrawer: () -> Unit,
    viewModel: EmpleadoListViewModel = hiltViewModel(),
    onAddEmpleado: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onAddEmpleado()
            viewModel.onEvent(EmpleadoListUiEvent.NavigationConsumed)
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
            viewModel.onEvent(EmpleadoListUiEvent.NavigationConsumed)
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(EmpleadoListUiEvent.ShowMessage(""))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de empleados") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = onAddEmpleado) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar empleado"
                )
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when {

                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.empleados.isEmpty() -> {
                    Text(
                        text = "No hay empleados",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.empleados,
                            key = { it.empleadoId }
                        ) { empleado ->

                            EmpleadoItem(
                                empleado = empleado,
                                onDelete = {
                                    viewModel.onEvent(
                                        EmpleadoListUiEvent.Delete(empleado.empleadoId)
                                    )
                                },
                                onClick = {
                                    viewModel.onEvent(
                                        EmpleadoListUiEvent.Edit(empleado.empleadoId)
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
fun EmpleadoItem(
    empleado: Empleado,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = empleado.nombres,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Sueldo: ${empleado.sueldo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar empleado"
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar empleado"
                )
            }
        }
    }
}