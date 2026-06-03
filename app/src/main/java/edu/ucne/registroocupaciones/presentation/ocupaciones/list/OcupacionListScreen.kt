package edu.ucne.registroocupaciones.presentation.ocupaciones.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.ocupaciones.model.Ocupacion

@Composable
fun OcupacionListScreen(
    onDrawer: () -> Unit,
    goToOcupacion: (Int) -> Unit,
    createOcupacion: () -> Unit,
    viewModel: OcupacionListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    OcupacionListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            when (event) {
                is OcupacionListUiEvent.Edit -> goToOcupacion(event.id)
                OcupacionListUiEvent.CreateNew -> createOcupacion()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OcupacionListBody(
    state: OcupacionListUiState,
    onDrawer: () -> Unit,
    onEvent: (OcupacionListUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Listado de Ocupaciones") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(OcupacionListUiEvent.CreateNew) }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(state.ocupaciones) { ocupacion ->
                    OcupacionCard(
                        ocupacion = ocupacion,
                        onClick = { onEvent(OcupacionListUiEvent.Edit(ocupacion.ocupacionId)) },
                        onDelete = { onEvent(OcupacionListUiEvent.Delete(ocupacion.ocupacionId)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun OcupacionCard(
    ocupacion: Ocupacion,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ocupacion.descripcion, style = MaterialTheme.typography.titleMedium)
                Text("Sueldo: ${ocupacion.sueldo}", style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = { onClick() }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OcupacionListPreview() {
    MaterialTheme {
        OcupacionListBody(
            state = OcupacionListUiState(
                ocupaciones = listOf(
                    Ocupacion(1, "Ingeniero de datos", 85000.0),
                    Ocupacion(2, "Abogado", 65000.0)
                )
            ),
            onDrawer = {},
            onEvent = {}
        )
    }
}