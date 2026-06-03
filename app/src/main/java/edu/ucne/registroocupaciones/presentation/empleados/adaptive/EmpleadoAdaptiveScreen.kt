package edu.ucne.registroocupaciones.presentation.empleados.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import edu.ucne.registroocupaciones.presentation.empleados.edit.EmpleadoEditScreen
import edu.ucne.registroocupaciones.presentation.empleados.list.EmpleadoListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun EmpleadoAdaptiveScreen(
    onDrawer: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,

        listPane = {
            AnimatedPane {
                EmpleadoListScreen(
                    onDrawer = onDrawer,
                    onNavigateToEdit = { empleadoId ->
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, empleadoId)
                        }
                    },
                    onAddEmpleado = {
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, 0)
                        }
                    }
                )
            }
        },

        detailPane = {
            AnimatedPane {
                val selectedEmpleadoId = navigator.currentDestination?.contentKey ?: 0
                val configuration = LocalConfiguration.current
                val mostrarBotonAtras = configuration.screenWidthDp < 600

                EmpleadoEditScreen(
                    empleadoId = selectedEmpleadoId,
                    canNavigateBack = mostrarBotonAtras,
                    goBack = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    }
                )
            }
        }
    )
}