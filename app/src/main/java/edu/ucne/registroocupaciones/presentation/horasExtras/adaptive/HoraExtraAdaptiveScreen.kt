package edu.ucne.registroocupaciones.presentation.horasExtras.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import edu.ucne.registroocupaciones.presentation.horasExtras.edit.HoraExtraEditScreen
import edu.ucne.registroocupaciones.presentation.horasExtras.list.HoraExtraListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HoraExtraAdaptiveScreen(
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
                HoraExtraListScreen(
                    onDrawer = onDrawer,
                    onNavigateToEdit = { horaExtraId ->
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, horaExtraId)
                        }
                    },
                    onAddHoraExtra = {
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, 0)
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val selectedHoraExtraId = navigator.currentDestination?.contentKey as? Int ?: 0
                val configuration = LocalConfiguration.current
                val mostrarBotonAtras = configuration.screenWidthDp < 600

                HoraExtraEditScreen(
                    horaExtraId = selectedHoraExtraId,
                    canNavigateBack = mostrarBotonAtras,
                    onBack = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    }
                )
            }
        }
    )
}