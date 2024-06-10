package com.blink.blinkid.ui.student

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.Exam
import com.blink.blinkid.ui.teacher.CircularButton
import com.blink.blinkid.ui.teacher.ExamList
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.ui.teacher.ProgressBar
import com.blink.blinkid.viewmodel.LoginViewModel
import com.blink.blinkid.viewmodel.StudentViewModel

@Composable
fun StudentDashBoard(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    viewModel: StudentViewModel = hiltViewModel()
) {
    var examList by remember { mutableStateOf<List<Exam>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val result by viewModel.studentExams.collectAsState(NetworkResult.Initial)

    LaunchedEffect(true) {
        viewModel.getStudentExams()
    }


    when (result) {
        is NetworkResult.Success -> {
            isLoading = false
            Log.e(
                "StudentDashBoard",
                "StudentDashBoard: ${result as NetworkResult.Success<List<Exam>>}"
            )
            examList = (result as NetworkResult.Success<List<Exam>>).body!!
        }

        is NetworkResult.Error -> {
            isLoading = false

            Log.e(
                "StudentDashBoard",
                "StudentDashBoard: ${result as NetworkResult.Error<List<Exam>>}"
            )
        }

        is NetworkResult.Loading -> {
            Log.e("StudentDashBoard", "StudentDashBoard: loading...")
            isLoading = true
        }

        else -> {
            Log.e("StudentDashBoard", "StudentDashBoard: else...")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderText("Welcome ${loginViewModel.getUser()?.username ?: ""}")


        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {

            if (isLoading) {
                ProgressBar()
            } else {
                if (examList.isEmpty()) {
                    Text(
                        text = "No exams for now. Enjoy :)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                } else {
                    ExamList(examList) {
                    }
                }
            }

            CircularButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                icon = Icons.Default.ExitToApp,
                backgroundColor = Color.Red
            ) {
                loginViewModel.logout()
                navController.navigate(Navigation.Routes.LOGIN) {
                    launchSingleTop = true
                    restoreState = false
                }
            }
        }
    }
}
