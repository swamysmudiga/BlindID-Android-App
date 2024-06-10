package com.blink.blinkid.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.R
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.viewmodel.LoginViewModel


@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {

    val context = LocalContext.current

    if (loginViewModel.isLoggedIn()) {
        if (loginViewModel.isTeacher())
            navController.navigate(Navigation.Routes.HOME)
        else if (loginViewModel.isStaff())
            navController.navigate(Navigation.Routes.STAFF_HOME)
        else
            navController.navigate(Navigation.Routes.STUDENT_DASHBOARD)
    }

    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        toastMessage.takeIf { it.isNotEmpty() }?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            toastMessage = ""
        }
    }

    val loginResponse by loginViewModel.loginResponse.collectAsState(initial = NetworkResult.Initial)

    LaunchedEffect(loginResponse) {
        when (loginResponse) {
            is NetworkResult.Initial -> {

            }

            is NetworkResult.Success -> {
                toastMessage = "Login successful"
                if (loginViewModel.isTeacher())
                    navController.navigate(Navigation.Routes.HOME)
                else if (loginViewModel.isStaff())
                    navController.navigate(Navigation.Routes.STAFF_HOME)
                else
                    navController.navigate(Navigation.Routes.STUDENT_DASHBOARD)
            }

            is NetworkResult.Error -> {
                toastMessage =
                    "Login failed: ${(loginResponse as NetworkResult.Error).errorMessage}"
            }

            else -> {
                toastMessage = "Loading..."
            }
        }
    }


    // UI code for login screen
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "",
            modifier = Modifier
                .size(300.dp)
                .padding(8.dp)
            )
        Text(
            text = "Login",
            fontSize = 24.sp,
            modifier = Modifier.padding(20.dp)
        )
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    toastMessage = "Email and password cannot be empty"
                    return@Button
                }
                loginViewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Login")
        }

    }

}
