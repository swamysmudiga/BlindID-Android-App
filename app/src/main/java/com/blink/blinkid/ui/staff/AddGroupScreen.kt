package com.blink.blinkid.ui.staff

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.Group
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.ui.teacher.ProgressBar
import com.blink.blinkid.viewmodel.GroupViewModel


@Composable
fun AddGroupScreen(
    navController: NavController,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            toastMessage = ""
        }
    }

    val response by viewModel.group.collectAsState(initial = NetworkResult.Initial)

    LaunchedEffect(response) {
        when (response) {
            is NetworkResult.Success -> {
                toastMessage = "Group added successfully"
                navController.popBackStack()
            }

            is NetworkResult.Error -> {
                toastMessage = "Error adding Group"
            }

            else -> {

            }
        }
    }


    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column {
        HeaderText(text = "Add Group")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isEmpty() || description.isEmpty()) {
                        toastMessage = "Please fill all fields"
                    } else {
                        viewModel.addGroup(
                            Group(
                                name = name,
                                description = description,
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Add Group")
            }

            if (response is NetworkResult.Loading) {
                ProgressBar()
            }

        }
    }
}
