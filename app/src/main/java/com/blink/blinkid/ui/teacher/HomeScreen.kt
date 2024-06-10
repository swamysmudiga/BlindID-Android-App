package com.blink.blinkid.ui.teacher

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.ui.ConfirmationDialog
import com.blink.blinkid.ui.rememberConfirmationDialogState
import com.blink.blinkid.viewmodel.LoginViewModel


@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel) {

    LaunchedEffect(true) {
        if (!loginViewModel.isLoggedIn()) {
            navController.navigate(Navigation.Routes.LOGIN)
        }
        Log.e("HomeScreen", "HomeScreen: ${loginViewModel.getUser()}")
    }
    Column {
        HeaderText(text = "Welcome ${loginViewModel.getUser()?.username ?: ""}")

        val confirmationDialogState = rememberConfirmationDialogState()

        if (confirmationDialogState.value) {
            ConfirmationDialog(
                dialogState = confirmationDialogState,
                onConfirmClick = {
                    loginViewModel.logout()
                    navController.navigate(Navigation.Routes.LOGIN) {
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                title = "Delete",
                text = "Are you sure you want to delete?"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.EXAMS)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Text(text = "Exams")
            }

            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.ADD_EXAM)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Add exam")
            }

            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.STUDENT_LIST)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Student list")
            }

            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.ADD_STUDENT)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Add student")
            }

            Button(
                onClick = {
                    confirmationDialogState.showConfirmationDialog()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Logout")
            }
        }
    }
}
