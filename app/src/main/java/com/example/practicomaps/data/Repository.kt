package com.example.practicomaps.data

import com.example.practicomaps.network.ApiService
import com.example.practicomaps.network.RetrofitClient
import retrofit2.Response

class Repository(private val api: ApiService = RetrofitClient.api) {


    suspend fun getRoutesByUser(username: String): Response<List<Route>> =
        api.getRoutesByUser(username)

    suspend fun createRoute(name: String, username: String): Response<Route> =
        api.insertRoute(mapOf("name" to name, "username" to username))


    suspend fun editRoute(id: Int, newName: String, username: String): Response<Route> =
        api.updateRoute(id, mapOf("name" to newName, "username" to username))

    suspend fun deleteRoute(id: Int): Response<Unit> =
        api.deleteRoute(id)


    suspend fun getLocationsByRoute(routeId: Int): Response<List<LocationPoint>> =
        api.getLocationsByRoute(routeId)

    suspend fun createLocation(lat: String, lng: String, routeId: Int): Response<LocationPoint> =
        api.insertLocation(mapOf("latitude" to lat, "longitude" to lng, "route_id" to routeId))


    suspend fun editLocation(id: Int, newLat: String, newLng: String): Response<LocationPoint> =
        api.updateLocation(id, mapOf("latitude" to newLat, "longitude" to newLng))

    suspend fun deleteLocation(id: Int): Response<Unit> =
        api.deleteLocation(id)
}
