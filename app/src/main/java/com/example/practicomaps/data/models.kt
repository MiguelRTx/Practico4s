package com.example.practicomaps.data


data class Route(
    val id: Int,
    val name: String,
    val username: String
)

data class LocationPoint(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val route_id: Int
)
