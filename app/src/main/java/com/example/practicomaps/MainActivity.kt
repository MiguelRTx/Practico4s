package com.example.practicomaps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicomaps.navigation.AppNavHost
import com.example.practicomaps.ui.theme.PracticoMapsTheme
import com.example.practicomaps.viewmodel.RouteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticoMapsTheme() {
                val vm: RouteViewModel = viewModel()
                AppNavHost(viewModel = vm)
            }
        }
    }
}
