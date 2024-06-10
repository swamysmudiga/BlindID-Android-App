package com.blink.blinkid.ui.teacher

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.blink.blinkid.BuildConfig
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.StudentExamValidations
import com.blink.blinkid.ui.UserCard
import com.blink.blinkid.ui.createImageFile
import com.blink.blinkid.viewmodel.ExamViewModel
import com.blink.blinkid.viewmodel.ValidateImageViewModel
import java.util.Objects


@Composable
fun StudentExamVerificationScreen(
    navController: NavController,
    viewModel: ExamViewModel,
    validateImageViewModel: ValidateImageViewModel = hiltViewModel()
) {

    val exam by viewModel.selectedExam.collectAsState()

    val student by viewModel.selectedUser.collectAsState()

    val res by validateImageViewModel.result.collectAsState()

    val error by validateImageViewModel.error.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(res, error) {
        if (res is NetworkResult.Success) {
//            Toast.makeText(context, "Validation success", Toast.LENGTH_SHORT).show()
            exam?.let { viewModel.setSelectedExam(it) }
            navController.popBackStack()
        } else if (res is NetworkResult.Error) {
            Toast.makeText(
                context,
                (res as NetworkResult.Error<StudentExamValidations>).errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
        if (error.isNotEmpty()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }


    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
            if (student?.images?.isEmpty() == true) {
                Toast.makeText(context, "No image found", Toast.LENGTH_SHORT).show()
                return@rememberLauncherForActivityResult
            }
            validateImageViewModel.uploadImage(
                part = capturedImageUri,
                examId = exam?.id ?: 0,
                studentId = student?.id ?: 0,
                list = student?.images?.map { it.url } ?: emptyList()
            )
        }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {

        HeaderText("Student Exam Verification")
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.padding(5.dp))

            exam?.let {
                ExamCard(exam = it) {

                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            student?.let {
                UserCard(user = it) {

                }
            }
            Spacer(modifier = Modifier.padding(5.dp))

            Button(
                onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Validate student")
            }

            if (res is NetworkResult.Loading) {
                Spacer(modifier = Modifier.height(10.dp))
                ProgressBar()
            }

            exam?.examValidations?.let { validations ->
                if (validations.any { it.studentId == student?.id }) {
                    LazyColumn {
                        items(validations.filter { it.studentId == student?.id }.size) { validation ->
                            UserValidationRow(studentExamValidations = validations.filter { it.studentId == student?.id }[validation])
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(5.dp))


        }
    }

}


@Composable
fun UserValidationRow(studentExamValidations: StudentExamValidations) {

    Row(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .graphicsLayer(shadowElevation = 2.0f)
            .fillMaxWidth()
            .background(if (studentExamValidations.status) Color(0xFF35704D) else MaterialTheme.colorScheme.error)
            .padding(12.dp)

    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(studentExamValidations.image)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "Image of Name",
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .padding(5.dp)
                .clip(CircleShape)
                .graphicsLayer(shadowElevation = 2.0f)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
                .height(60.dp)
        ) {

            Text(
                text = if (studentExamValidations.status) "Validation success" else "Validation failed",
                fontFamily = FontFamily.Default,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}