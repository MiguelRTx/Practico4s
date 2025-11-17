package com.example.practicomaps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicomaps.viewmodel.RouteViewModel
import com.example.practicomaps.screen.LoginScreen
import com.example.practicomaps.screen.RoutesListScreen
import com.example.practicomaps.screen.MapScreen
import com.example.practicomaps.screen.SplashScreen


@Composable
fun AppNavHost(
    viewModel: RouteViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController = navController, onLogin = {
                viewModel.loadRoutesForUser(it)
                navController.navigate(Routes.RoutesList.route.replace("{user}", it))
            })
        }

        composable(
            Routes.RoutesList.route,
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            RoutesListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.MapScreen.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getInt("id") ?: 0
            val routeName = backStackEntry.arguments?.getString("name") ?: ""

            MapScreen(
                routeId = routeId,
                routeName = routeName,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}