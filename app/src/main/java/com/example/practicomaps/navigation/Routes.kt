package com.example.practicomaps.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object RoutesList : Routes("routes/{user}")
    object MapScreen : Routes("map/{id}/{name}")
}