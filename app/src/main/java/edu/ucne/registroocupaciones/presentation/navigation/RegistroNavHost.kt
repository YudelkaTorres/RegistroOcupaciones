package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroocupaciones.presentation.ocupaciones.edit.OcupacionEditScreen
import edu.ucne.registroocupaciones.presentation.ocupaciones.list.OcupacionListScreen
import edu.ucne.registroocupaciones.presentation.empleados.edit.EmpleadoEditScreen
import edu.ucne.registroocupaciones.presentation.empleados.list.EmpleadoListScreen
import kotlinx.coroutines.launch

@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.OcupacionList
        ) {
            composable<Screen.OcupacionList> {

                OcupacionListScreen(
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    },
                    goToOcupacion = { id ->
                        navHostController.navigate(Screen.OcupacionEdit(id))
                    },
                    createOcupacion = {
                        navHostController.navigate(Screen.OcupacionEdit(0))
                    }
                )
            }

            composable<Screen.OcupacionEdit> { backStackEntry ->

                val args = backStackEntry.toRoute<Screen.OcupacionEdit>()

                OcupacionEditScreen(
                    ocupacionId = args.ocupacionId,
                    goBack = {
                        navHostController.navigateUp()
                    },
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            composable<Screen.EmpleadoList> {

                EmpleadoListScreen(
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    },
                    onAddEmpleado = {
                        navHostController.navigate(Screen.EmpleadoEdit(0))
                    },
                    onNavigateToEdit = { id ->
                        navHostController.navigate(Screen.EmpleadoEdit(id))
                    }
                )
            }

            composable<Screen.EmpleadoEdit> { backStackEntry ->

                val args = backStackEntry.toRoute<Screen.EmpleadoEdit>()

                EmpleadoEditScreen(
                    empleadoId = args.empleadoId,
                    goBack = {
                        navHostController.navigateUp()
                    },
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}