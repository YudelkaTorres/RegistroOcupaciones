package edu.ucne.registroocupaciones.presentation.horasExtras.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroocupaciones.domain.horasExtras.model.TipoHoraExtra
import edu.ucne.registroocupaciones.presentation.empleados.edit.EmpleadoEditUiEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraExtraEditScreen(
    horaExtraId: Int,
    canNavigateBack: Boolean = true,
    viewModel: HoraExtraEditViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val empleados by viewModel.empleados
        .collectAsStateWithLifecycle()

    var expandedEmpleado by remember {
        mutableStateOf(false)
    }

    var expandedTipo by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(horaExtraId) {
        viewModel.onEvent(HoraExtraEditUiEvent.Load(horaExtraId))
    }

    LaunchedEffect(state.saved) {
        if (state.saved){
            onBack()
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        if (state.isNew)
                            "Nueva Hora Extra"
                        else
                            "Editar Hora Extra"
                    )
                },

                actions = {
                    if (canNavigateBack){
                        IconButton(
                            onClick = onBack
                        ) {

                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atras"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            ExposedDropdownMenuBox(

                expanded = expandedEmpleado,

                onExpandedChange = {
                    expandedEmpleado = !expandedEmpleado
                }
            ) {

                val empleadoSeleccionado =
                    empleados.find {
                        it.empleadoId ==
                                state.empleadoId
                    }

                OutlinedTextField(

                    value =
                        empleadoSeleccionado?.nombres
                            ?: "",

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Empleado")
                    },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    expandedEmpleado
                            )
                    },

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),

                    isError =
                        state.empleadoError != null,

                    supportingText =
                        state.empleadoError?.let {
                            { Text(it) }
                        }
                )

                ExposedDropdownMenu(

                    expanded = expandedEmpleado,

                    onDismissRequest = {
                        expandedEmpleado = false
                    }
                ) {

                    empleados.forEach { empleado ->

                        DropdownMenuItem(

                            text = {

                                Text(
                                    empleado.nombres
                                )
                            },

                            onClick = {

                                viewModel.onEvent(

                                    HoraExtraEditUiEvent
                                        .EmpleadoChanged(
                                            empleado.empleadoId
                                        )
                                )

                                expandedEmpleado =
                                    false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(

                expanded = expandedTipo,

                onExpandedChange = {
                    expandedTipo = !expandedTipo
                }
            ) {

                OutlinedTextField(

                    value = when (state.tipoHoraExtra) {

                            TipoHoraExtra.JORNADA_ORDINARIA -> "Jornada Ordinaria"

                            TipoHoraExtra.DESCANSO_FERIADO -> "Descanso/Feriado"
                            null -> "Seleccione un tipo"
                        },

                        onValueChange = {},

                        readOnly = true,

                        label = { Text("Tipo Hora Extra") },

                        isError = state.tipoHoraExtraError != null,

                        supportingText = state.tipoHoraExtraError?.let {
                            { Text(it) }
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                )

                ExposedDropdownMenu(

                    expanded = expandedTipo,

                    onDismissRequest = {
                        expandedTipo = false
                    }
                ) {

                    DropdownMenuItem(

                        text = {
                            Text(
                                "Jornada Ordinaria"
                            )
                        },

                        onClick = {

                            viewModel.onEvent(

                                HoraExtraEditUiEvent
                                    .TipoHoraExtraChanged(
                                        TipoHoraExtra
                                            .JORNADA_ORDINARIA
                                    )
                            )

                            expandedTipo = false
                        }
                    )

                    DropdownMenuItem(

                        text = {
                            Text(
                                "Descanso/Feriado"
                            )
                        },

                        onClick = {

                            viewModel.onEvent(

                                HoraExtraEditUiEvent
                                    .TipoHoraExtraChanged(
                                        TipoHoraExtra
                                            .DESCANSO_FERIADO
                                    )
                            )

                            expandedTipo = false
                        }
                    )
                }
            }

            OutlinedTextField(

                value = state.horasTrabajadas,

                onValueChange = {

                    viewModel.onEvent(

                        HoraExtraEditUiEvent
                            .HorasTrabajadasChanged(it)
                    )
                },

                label = {
                    Text("Horas Semanales")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_horas"),

                isError =
                    state.horasError != null,

                supportingText =
                    state.horasError?.let {
                        { Text(it) }
                    },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                singleLine = true
            )

            OutlinedTextField(

                value = state.horasNocturnas,

                onValueChange = {

                    viewModel.onEvent(

                        HoraExtraEditUiEvent
                            .HorasNocturnasChanged(it)
                    )
                },

                label = {
                    Text("Horas Nocturnas")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_nocturnas"),

                isError =
                    state.horasNocturnasError != null,

                supportingText =
                    state.horasNocturnasError?.let {
                        { Text(it) }
                    },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                singleLine = true
            )

            HorizontalDivider()

            Text(
                text = "Cálculos",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "Sueldo Mensual: RD$ ${state.sueldoMensual}"
            )

            Text(
                text =
                    "Sueldo Diario: RD$ ${state.sueldoDiario}"
            )

            Text(
                text =
                    "Sueldo por Hora: RD$ ${state.sueldoHora}"
            )

            Text(
                text =
                    "Horas al 35%: ${state.horas35}"
            )

            Text(
                text =
                    "Horas al 100%: ${state.horas100}"
            )

            Text(
                text =
                    "Monto 35%: RD$ ${state.monto35}"
            )

            Text(
                text =
                    "Monto 100%: RD$ ${state.monto100}"
            )

            Text(
                text =
                    "Monto Nocturno: RD$ ${state.montoNocturno}"
            )

            Text(
                text =
                    "Monto Total: RD$ ${state.montoTotal}",

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(HoraExtraEditUiEvent.Save)
                    },
                    enabled = !state.isSaving
                ) {
                    Icon(Icons.Default.Edit, null)
                    Text("Guardar")
                }

                if (!state.isNew) {

                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(HoraExtraEditUiEvent.Delete)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}