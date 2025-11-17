package com.example.practicomaps.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicomaps.data.Repository
import com.example.practicomaps.data.Route
import com.example.practicomaps.data.LocationPoint
import kotlinx.coroutines.launch

class RouteViewModel(private val repo: Repository = Repository()) : ViewModel() {
    val username = mutableStateOf("")
    val routes = mutableStateListOf<Route>()
    val locations = mutableStateListOf<LocationPoint>()
    val loading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)


    fun loadRoutesForUser(user: String) {
        username.value = user
        loading.value = true
        error.value = null
        viewModelScope.launch {
            try {
                val resp = repo.getRoutesByUser(user)
                if (resp.isSuccessful) {
                    routes.clear()
                    resp.body()?.let { routes.addAll(it) }
                } else {
                    error.value = "Error al cargar rutas: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun createRoute(name: String) {
        val user = username.value.ifBlank { return }
        loading.value = true
        viewModelScope.launch {
            try {
                val resp = repo.createRoute(name, user)
                if (resp.isSuccessful) {
                    resp.body()?.let { routes.add(it) }
                } else {
                    error.value = "Error creando ruta: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }


    fun editRoute(routeId: Int, newName: String) {
        val routeToUpdate = routes.find { it.id == routeId } ?: return
        loading.value = true
        error.value = null
        val user = username.value
        if (user.isBlank()) {

            error.value = "Error: Usuario no identificado."
            loading.value = false
            return
        }
        viewModelScope.launch {
            try {
                val resp = repo.editRoute(routeId, newName, user)
                if (resp.isSuccessful) {
                    val updatedRoute = routeToUpdate.copy(name = newName)
                    val index = routes.indexOf(routeToUpdate)
                    if (index != -1) {
                        routes[index] = updatedRoute
                    }
                } else {
                    error.value = "Error editando ruta: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun deleteRoute(routeId: Int, onComplete: (() -> Unit)? = null) {
        loading.value = true
        viewModelScope.launch {
            try {
                val resp = repo.deleteRoute(routeId)
                if (resp.isSuccessful) {
                    routes.removeAll { it.id == routeId }
                    onComplete?.invoke()
                } else {
                    error.value = "Error eliminando ruta: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun loadLocationsForRoute(routeId: Int) {
        loading.value = true
        viewModelScope.launch {
            try {
                val resp = repo.getLocationsByRoute(routeId)
                if (resp.isSuccessful) {
                    locations.clear()
                    resp.body()?.let { locations.addAll(it) }
                } else {
                    error.value = "Error cargando puntos: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun createLocation(lat: String, lng: String, routeId: Int, onComplete: (() -> Unit)? = null) {
        loading.value = true
        viewModelScope.launch {
            try {
                val resp = repo.createLocation(lat, lng, routeId)
                if (resp.isSuccessful) {
                    resp.body()?.let { locations.add(it) }
                    onComplete?.invoke()
                } else {
                    error.value = "Error creando punto: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun editLocation(locationId: Int, newLat: String, newLng: String) {
        val locationToUpdate = locations.find { it.id == locationId } ?: return
        loading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val resp = repo.editLocation(locationId, newLat, newLng)
                if (resp.isSuccessful) {
                    resp.body()?.let { updatedLocation ->
                        val index = locations.indexOf(locationToUpdate)
                        if (index != -1) {
                            locations[index] = updatedLocation
                        }
                    }
                } else {
                    error.value = "Error editando punto: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }

    fun deleteLocation(id: Int, onComplete: (() -> Unit)? = null) {
        loading.value = true
        viewModelScope.launch {
            try {
                val resp = repo.deleteLocation(id)
                if (resp.isSuccessful) {
                    locations.removeAll { it.id == id }
                    onComplete?.invoke()
                } else {
                    error.value = "Error eliminando punto: ${resp.code()}"
                }
            } catch (e: Exception) {
                error.value = "Excepción: ${e.localizedMessage}"
            } finally {
                loading.value = false
            }
        }
    }
}