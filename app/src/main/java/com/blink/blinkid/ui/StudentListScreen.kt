package com.blink.blinkid.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.User
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.ui.teacher.ProgressBar
import com.blink.blinkid.viewmodel.ExamViewModel


@Composable
fun StudentListScreen(
    navController: NavController,
    examViewModel: ExamViewModel,
) {

    var studentList by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val result by examViewModel.students.collectAsState(NetworkResult.Initial)

    LaunchedEffect(true) {
        examViewModel.getStudents()
    }
    when (result) {
        is NetworkResult.Success -> {
            isLoading = false
            studentList = (result as NetworkResult.Success<List<User>>).body!!
        }

        is NetworkResult.Error -> {
            isLoading = false
        }

        is NetworkResult.Loading -> {
            Log.e("studentListScreen", "studentListScreen: loading...")
            isLoading = true
        }

        else -> {
            Log.e("studentListScreen", "studentListScreen: else...")
        }
    }

    Column {
        HeaderText("Students")

        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color(0xFFF8F8F8))
        ) {

            if (isLoading) {
                ProgressBar()
            } else {
                StudentList(true,studentList) {

                }
            }
        }
    }

}