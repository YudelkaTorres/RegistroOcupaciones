package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroocupaciones.presentation.ocupacion.edit.OcupacionEditScreen
import edu.ucne.registroocupaciones.presentation.ocupacion.list.OcupacionListScreen

@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {

    NavHost(
        navController = navHostController,
        startDestination = Screen.OcupacionList
    ) {
        composable<Screen.OcupacionList> {

            OcupacionListScreen(
                onDrawer = { },
                goToOcupacion = { id ->
                    navHostController.navigate(Screen.OcupacionEdit(id))
                },
                createOcupacion = {
                    navHostController.navigate(Screen.OcupacionEdit(0))
                },
            )
        }

        composable<Screen.OcupacionEdit> { backStateEntry ->
            val args = backStateEntry.toRoute<Screen.OcupacionEdit>()
            OcupacionEditScreen(
                ocupacionId = args.ocupacionId,
                goBack = {
                    navHostController.navigateUp()
                },
                onDrawer = { }
            )
        }
    }
}