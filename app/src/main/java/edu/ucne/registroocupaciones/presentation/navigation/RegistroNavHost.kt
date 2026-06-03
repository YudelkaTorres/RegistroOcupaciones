package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.ucne.registroocupaciones.presentation.ocupaciones.adaptive.OcupacionAdaptiveScreen
import edu.ucne.registroocupaciones.presentation.empleados.adaptive.EmpleadoAdaptiveScreen
import edu.ucne.registroocupaciones.presentation.horasExtras.adaptive.HoraExtraAdaptiveScreen

@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                icon = { Icon(Icons.Default.Work, contentDescription = "Ocupaciones") },
                label = { Text("Ocupaciones") },

                selected = currentDestination?.contains("OcupacionList") == true,
                onClick = { navHostController.navigate(Screen.OcupacionList) }
            )
            item(
                icon = { Icon(Icons.Default.People, contentDescription = "Empleados") },
                label = { Text("Empleados") },
                selected = currentDestination?.contains("EmpleadoList") == true,
                onClick = { navHostController.navigate(Screen.EmpleadoList) }
            )
            item(
                icon = { Icon(Icons.Default.Schedule, contentDescription = "Horas Extras") },
                label = { Text("Horas Extras") },
                selected = currentDestination?.contains("HoraExtraList") == true,
                onClick = { navHostController.navigate(Screen.HoraExtraList) }
            )
        }
    ) {

        NavHost(
            navController = navHostController,
            startDestination = Screen.OcupacionList
        ) {
            composable<Screen.OcupacionList> {
                OcupacionAdaptiveScreen(
                    onDrawer = {}
                )
            }

            composable<Screen.EmpleadoList> {
                EmpleadoAdaptiveScreen(
                    onDrawer = {}
                )
            }

            composable<Screen.HoraExtraList> {
                HoraExtraAdaptiveScreen(
                    onDrawer = {}
                )
            }
        }
    }
}