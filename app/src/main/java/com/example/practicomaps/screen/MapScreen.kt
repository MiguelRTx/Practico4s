package com.example.practicomaps.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.practicomaps.viewmodel.RouteViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    routeId: Int,
    routeName: String,
    viewModel: RouteViewModel,
    onBack: () -> Unit
) {
    val points = viewModel.locations
    val cameraPositionState = rememberCameraPositionState()
    var initialCameraMoveDone by remember(routeId) { mutableStateOf(false) }


    LaunchedEffect(routeId) {
        viewModel.loadLocationsForRoute(routeId)
    }


    LaunchedEffect(points.size) {
        if (!initialCameraMoveDone && points.isNotEmpty()) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(
                    points.first().latitude.toDouble(),
                    points.first().longitude.toDouble()
                ),
                16f
            )
            initialCameraMoveDone = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routeName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val lat = cameraPositionState.position.target.latitude.toString()
                val lng = cameraPositionState.position.target.longitude.toString()
                viewModel.createLocation(lat, lng, routeId)
            }) {
                Text("Agregar punto")
            }
        }
    ) { padding ->

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.createLocation(
                    lat = latLng.latitude.toString(),
                    lng = latLng.longitude.toString(),
                    routeId = routeId
                )
            }
        ) {


            points.forEach { point ->
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            point.latitude.toDouble(),
                            point.longitude.toDouble()
                        )
                    ),
                    title = "Punto ${point.id}",
                    snippet = "Tocar para eliminar",
                    onInfoWindowClick = {
                        viewModel.deleteLocation(point.id)
                    }
                )
            }
            val segmentos = points.chunked(2)

            segmentos.forEach { par ->
                if (par.size == 2) {
                    Polyline(
                        points = par.map {
                            LatLng(
                                it.latitude.toDouble(),
                                it.longitude.toDouble()
                            )
                        },
                        color = Color.Blue,
                        width = 8f
                )
            }
        }
    }
}
}
