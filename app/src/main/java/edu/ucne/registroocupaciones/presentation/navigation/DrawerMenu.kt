package edu.ucne.registroocupaciones.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf("Ocupaciones") }
    val scope = rememberCoroutineScope()

    fun navigate(screen: Screen, item: String) {
        navHostController.navigate(screen) {
            launchSingleTop = true
        }
        selectedItem.value = item
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Registro Ocupaciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                LazyColumn {
                    item {

                        NavigationDrawerItem(
                            label = { Text("Ocupaciones") },
                            selected = selectedItem.value == "Ocupaciones",
                            icon = { Icon(Icons.Default.Work, contentDescription = null) },
                            onClick = {
                                navigate(Screen.OcupacionList, "Ocupaciones")
                            }
                        )

                        NavigationDrawerItem(
                            label = { Text("Empleados") },
                            selected = selectedItem.value == "Empleados",
                            icon = { Icon(Icons.Default.Badge, contentDescription = null) },
                            onClick = {
                                navigate(Screen.EmpleadoList, "Empleados")
                            }
                        )
                    }
                }
            }
        }
    ) {
        content()
    }
}