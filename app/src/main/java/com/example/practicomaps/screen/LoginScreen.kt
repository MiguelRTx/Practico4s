package com.example.practicomaps.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, onLogin: (String) -> Unit) {

    var username by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Ingrese su usuario:")

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (username.text.isNotBlank()) {
                        onLogin(username.text)
                    }
                }
            ) {
                Text("Ingresar")
            }
        }
    }
}