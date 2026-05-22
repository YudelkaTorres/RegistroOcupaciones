package edu.ucne.registroocupaciones.presentation.empleados.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoEditScreen(
    empleadoId: Int?,
    goBack: () -> Unit,
    onDrawer: () -> Unit
) {

    val viewModel: EmpleadoEditViewModel = hiltViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(empleadoId) {
        viewModel.onEvent(
            EmpleadoEditUiEvent.LoadEmpleado(empleadoId)
        )
    }

    if (uiState.saved || uiState.deleted) {
        LaunchedEffect(Unit) {
            goBack()
        }
    }

    val sexoOptions = listOf("Masculino", "Femenino")
    var sexoExpanded by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    if (uiState.showDatePicker) {

        DatePickerDialog(
            onDismissRequest = {
                viewModel.onEvent(
                    EmpleadoEditUiEvent.HideDatePicker
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let {

                            val fecha =
                                formatter.format(Date(it))

                            viewModel.onEvent(
                                EmpleadoEditUiEvent
                                    .FechaIngresoSelected(fecha)
                            )
                        }

                        viewModel.onEvent(
                            EmpleadoEditUiEvent.HideDatePicker
                        )
                    }
                ) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        if (uiState.isNew)
                            "Nuevo Empleado"
                        else
                            "Editar Empleado"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(Icons.Default.Menu, null)
                    }
                },
                actions = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {


            OutlinedTextField(
                value = uiState.fechaIngreso,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha Ingreso") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                EmpleadoEditUiEvent.ShowDatePicker
                            )
                        }
                    ) {
                        Icon(Icons.Default.CalendarToday, null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.onEvent(
                            EmpleadoEditUiEvent.ShowDatePicker
                        )
                    }
            )

            uiState.fechaIngresoError?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = uiState.nombres,
                onValueChange = {
                    viewModel.onEvent(
                        EmpleadoEditUiEvent.NombresChanged(it)
                    )
                },
                label = { Text("Nombres") },
                isError = uiState.nombresError != null,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.nombresError?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = {
                    sexoExpanded = !sexoExpanded
                }
            ) {

                OutlinedTextField(
                    value = uiState.sexo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, null)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = sexoExpanded,
                    onDismissRequest = {
                        sexoExpanded = false
                    }
                ) {

                    sexoOptions.forEach { option ->

                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.onEvent(
                                    EmpleadoEditUiEvent
                                        .SexoChanged(option)
                                )
                                sexoExpanded = false
                            }
                        )
                    }
                }
            }

            uiState.sexoError?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(10.dp))


            OutlinedTextField(
                value = uiState.sueldo ?: "",
                onValueChange = {
                    viewModel.onEvent(
                        EmpleadoEditUiEvent.SueldoChanged(it)
                    )
                },
                label = { Text("Sueldo") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = uiState.sueldoError != null,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.sueldoError?.let {
                Text(it, color = Color.Red)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(
                            EmpleadoEditUiEvent.Save
                        )
                    },
                    enabled = !uiState.isSaving
                ) {
                    Icon(Icons.Default.Edit, null)
                    Text("Guardar")
                }

                if (!uiState.isNew) {

                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(
                                EmpleadoEditUiEvent.Delete
                            )
                        },
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )
                    ) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
            }
        }
    }
}