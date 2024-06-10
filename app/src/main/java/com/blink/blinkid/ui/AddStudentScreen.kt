package com.blink.blinkid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.blink.blinkid.BuildConfig
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.AddStudentRequest
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.viewmodel.ExamViewModel
import com.blink.blinkid.viewmodel.ImageViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun AddStudentScreen(
    navController: NavController,
    viewModel: ExamViewModel = hiltViewModel(),
    imageViewModel: ImageViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            toastMessage = ""
        }
    }

    val imageResponse by imageViewModel.uploadedImage.collectAsState("")

    val error by imageViewModel.error.collectAsState(initial = "")

    LaunchedEffect(imageResponse) {
        if (imageResponse.isNotEmpty()) {
            Log.d("AddStudentScreen", "imageResponse: $imageResponse")
            toastMessage = "Image uploaded successfully"
        }
    }

    val response by viewModel.user.collectAsState(initial = NetworkResult.Initial)

    LaunchedEffect(response) {
        when (response) {
            is NetworkResult.Success -> {
                toastMessage = "Student added successfully"
                navController.popBackStack()
            }

            is NetworkResult.Error -> {
                toastMessage = "Error adding user"
            }

            else -> {

            }
        }
    }

    var password by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }


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
            imageViewModel.uploadImage(uri)
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
    Column {
        HeaderText(text = "Add Student")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (capturedImageUri.path?.isNotEmpty() == true) {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(8.dp, 8.dp)
                        .clip(CircleShape)
                        .graphicsLayer(shadowElevation = 2.0f),
                    painter = rememberAsyncImagePainter(capturedImageUri),
                    contentDescription = null,
                )
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text(text = "Add image")
                }
            }


            OutlinedTextField(
                value = mail,
                onValueChange = { mail = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    if (mail.isEmpty() || userName.isEmpty() || password.isEmpty() || imageResponse.isEmpty()) {
                        toastMessage = "All fields are required, Image also required"
                        return@Button
                    }

                    viewModel.addStudent(
                        AddStudentRequest(
                            email = mail,
                            password = password,
                            username = userName,
                            image = imageResponse
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add student")
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}
