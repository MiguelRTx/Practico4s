package com.example.practicomaps.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practicomaps.data.Route
import com.example.practicomaps.navigation.Routes
import com.example.practicomaps.viewmodel.RouteViewModel

@Composable
fun RoutesListScreen(
    navController: NavController,
    viewModel: RouteViewModel
) {
    val routes = viewModel.routes
    val loading = viewModel.loading.value
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.error
    var showEditDialog by remember { mutableStateOf(false) }
    var routeToEdit by remember { mutableStateOf<Route?>(null) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar("Error: $errorMessage")
            viewModel.error.value = null
        }
    }


    Scaffold(

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.createRoute("Nueva Ruta")
            }) {
                Text("+")
            }
        }
    ) { padding ->

        if (loading && routes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(routes) { ruta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate(
                                Routes.MapScreen.route
                                    .replace("{id}", ruta.id.toString())
                                    .replace("{name}", ruta.name)
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ruta.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))

                        IconButton(onClick = {
                            routeToEdit = ruta
                            showEditDialog = true
                        }) {
                            Icon(Icons.Default.Create, contentDescription = "Editar Ruta")
                        }

                        IconButton(onClick = { viewModel.deleteRoute(ruta.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar Ruta")
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog && routeToEdit != null) {
        EditRouteDialog(
            route = routeToEdit!!,
            onDismiss = { showEditDialog = false },
            onUpdate = { newName ->
                viewModel.editRoute(routeToEdit!!.id, newName)
                showEditDialog = false
            }
        )
    }
}


@Composable
fun EditRouteDialog(
    route: Route,
    onDismiss: () -> Unit,
    onUpdate: (String) -> Unit
) {

    var newRouteName by remember { mutableStateOf(route.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nombre de Ruta") },
        text = {
            OutlinedTextField(
                value = newRouteName,
                onValueChange = { newRouteName = it },
                label = { Text("Nuevo Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newRouteName.isNotBlank()) {
                        onUpdate(newRouteName)
                    }
                },
                enabled = newRouteName.isNotBlank()
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}