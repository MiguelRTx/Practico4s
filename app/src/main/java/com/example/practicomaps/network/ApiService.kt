package com.example.practicomaps.network

import com.example.practicomaps.data.LocationPoint
import com.example.practicomaps.data.Route
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @GET("api/routes")
    suspend fun getAllRoutes(): Response<List<Route>>

    @GET("api/routes/{username}")
    suspend fun getRoutesByUser(@Path("username") username: String): Response<List<Route>>

    @POST("api/routes")
    suspend fun insertRoute(@Body body: Map<String, @JvmSuppressWildcards Any>): Response<Route>

    @PUT("api/routes/{id}")
    suspend fun updateRoute(@Path("id") id: Int, @Body body: Map<String, @JvmSuppressWildcards Any>): Response<Route>

    @DELETE("api/routes/{id}")
    suspend fun deleteRoute(@Path("id") id: Int): Response<Unit>


    @GET("api/locations")
    suspend fun getAllLocations(): Response<List<LocationPoint>>

    @GET("api/routes/{routeId}/locations")
    suspend fun getLocationsByRoute(@Path("routeId") routeId: Int): Response<List<LocationPoint>>

    @POST("api/locations")
    suspend fun insertLocation(@Body body: Map<String, @JvmSuppressWildcards Any>): Response<LocationPoint>

    @PUT("api/locations/{id}")
    suspend fun updateLocation(@Path("id") id: Int, @Body body: Map<String, @JvmSuppressWildcards Any>): Response<LocationPoint>

    @DELETE("api/locations/{id}")
    suspend fun deleteLocation(@Path("id") id: Int): Response<Unit>
}