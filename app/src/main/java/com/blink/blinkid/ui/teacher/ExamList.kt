package com.blink.blinkid.ui.teacher

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.model.Exam
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.viewmodel.ExamViewModel

@Composable
fun ExamListScreen(
    navController: NavController,
    examViewModel: ExamViewModel,
) {
    var examList by remember { mutableStateOf<List<Exam>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val result by examViewModel.exams.collectAsState(NetworkResult.Initial)

    LaunchedEffect(true) {
        examViewModel.getExams()
    }
    when (result) {
        is NetworkResult.Success -> {
            isLoading = false
            Log.e(
                "ExamListScreen",
                "ExamListScreen: ${result as NetworkResult.Success<List<Exam>>}"
            )
            examList = (result as NetworkResult.Success<List<Exam>>).body!!
        }

        is NetworkResult.Error -> {
            isLoading = false

            Log.e(
                "ExamListScreen",
                "ExamListScreen: ${result as NetworkResult.Error<List<Exam>>}"
            )
        }

        is NetworkResult.Loading -> {
            Log.e("ExamListScreen", "ExamListScreen: loading...")
            isLoading = true
        }

        else -> {
            Log.e("ExamListScreen", "ExamListScreen: else...")
        }
    }

    Column {
        HeaderText("Exams")


        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color(0xFFF8F8F8))
        ) {

            if (isLoading) {
                ProgressBar()
            } else {
                if (examList.isEmpty()) {
                    Text(
                        text = "No exams available",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                } else {
                    ExamList(examList) {
                        examViewModel.setSelectedExam(it)
                        navController.navigate(Navigation.Routes.EXAM_DETAIL + "/${it.id}")
                    }
                }
            }
        }
    }

}


@Composable
fun ExamList(exams: List<Exam>, onUserClick: (Exam) -> Unit) {
    Log.e("ExamList", "ExamList: $exams")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        exams.forEach { exam ->
            ExamCard(exam, onUserClick)
        }
    }

}


@Composable
fun ProgressBar() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ExamCard(exam: Exam, onUserClick: (Exam) -> Unit) {

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .graphicsLayer(shadowElevation = 2.0f)
            .padding(12.dp)
            .clickable { onUserClick(exam) }
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = exam.name, fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = exam.examLocation)
        }

        Text(
            text = exam.description,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            if (exam.admins.isNotEmpty())
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Invigilator",
                        color = Color.Gray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = exam.admins[0].username,
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = exam.examDate,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = exam.examTime,
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )

            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
