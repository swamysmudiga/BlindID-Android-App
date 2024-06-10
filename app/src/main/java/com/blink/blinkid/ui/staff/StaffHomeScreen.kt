package com.blink.blinkid.ui.staff

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.R
import com.blink.blinkid.model.Group
import com.blink.blinkid.ui.ConfirmationDialog
import com.blink.blinkid.ui.rememberConfirmationDialogState
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.viewmodel.LoginViewModel


@Composable
fun StaffHomeScreen(navController: NavController, loginViewModel: LoginViewModel) {

    LaunchedEffect(true) {
        if (!loginViewModel.isLoggedIn()) {
            navController.navigate(Navigation.Routes.LOGIN)
        }
        Log.e("StaffHomeScreen", "HomeScreen: ${loginViewModel.getUser()}")
    }
    Column {
        HeaderText(text = "Welcome ${loginViewModel.getUser()?.username ?: ""}")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.GROUPS)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Groups")
            }

            Button(
                onClick = {
                    navController.navigate(Navigation.Routes.ADD_GROUP)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Add group")
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
                    title = "Logout",
                    text = "Are you sure you want to logout?"
                )
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


@Composable
@Preview(showBackground = true)
fun MenuOptionCardPreview() {
    MenuOptionCard("Group 1", R.drawable.exams) {

    }
}


@Composable
fun MenuOptionCard(name: String, image: Int, onClick: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,

        modifier = Modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .graphicsLayer(shadowElevation = 2.0f)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .padding(3.dp)
                .graphicsLayer(shadowElevation = 2.0f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = name, fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

    }
    Spacer(modifier = Modifier.height(10.dp))
}
