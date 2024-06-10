package com.blink.blinkid.ui.staff

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.blink.blinkid.Navigation
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.User
import com.blink.blinkid.ui.ConfirmationDialog
import com.blink.blinkid.ui.StudentList
import com.blink.blinkid.ui.UserCardWithDelete
import com.blink.blinkid.ui.rememberConfirmationDialogState
import com.blink.blinkid.ui.teacher.CircularButton
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.viewmodel.GroupViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(navController: NavController, viewModel: GroupViewModel) {

    val group by viewModel.selectedGroup.collectAsState()

    var students by remember { mutableStateOf<List<User>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }


    val studentsRes by viewModel.students.collectAsState()

    LaunchedEffect(true) {
        viewModel.getStudents()
    }

    when (studentsRes) {
        is NetworkResult.Success -> {
            isLoading = false
            students = (studentsRes as NetworkResult.Success<List<User>>).body!!
        }

        is NetworkResult.Error -> {
            isLoading = false
        }

        is NetworkResult.Loading -> {
            isLoading = true
        }

        else -> {

        }
    }


    Column {

        HeaderText("Group Details")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                group?.let {
                    GroupCard(group = it) {

                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = "Staff Member",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 0.dp, top = 10.dp, bottom = 10.dp)
                )

                StudentList(false, group?.admins ?: emptyList()) {
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = "Enrolled Students",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 0.dp, top = 10.dp, bottom = 10.dp)
                )

                LazyColumn {
                    group?.users?.let { list ->
                        items(list.size) { student ->
                            UserCardWithDelete(list[student], { userId ->
                                viewModel.deleteStudentFromGroup(group?.id!!, userId)
                            },
                                {
                                    viewModel.setSelectedUser(user = it)
                                    Log.d(
                                        "GroupDetailsScreen",
                                        "GroupDetailsScreen: ${it.username}"
                                    )
                                    navController.navigate(Navigation.Routes.STUDENT_GROUP_VERIFICATION)
                                })
                        }
                    }
                }

            }
            var sheetVisible by remember { mutableStateOf(false) }
            val confirmationDialogState = rememberConfirmationDialogState()

            CircularButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                sheetVisible = true
            }
            if (confirmationDialogState.value) {
                ConfirmationDialog(
                    dialogState = confirmationDialogState,
                    onConfirmClick = {
                        viewModel.deleteGroup(group?.id!!)
                        navController.popBackStack()
                    },
                    title = "Delete group",
                    text = "Are you sure you want to delete group?"
                )
            }

            CircularButton(
                modifier = Modifier.align(Alignment.BottomStart),
                icon = Icons.Default.Delete,
                backgroundColor = Color.Red
            ) {
                group?.id?.let {
                    confirmationDialogState.showConfirmationDialog()
                }
            }

            if (sheetVisible) {
                ModalBottomSheet(onDismissRequest = { sheetVisible = false }) {

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select student to add",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )

                        if (isLoading) {
                            // show loading
                            com.blink.blinkid.ui.teacher.ProgressBar()
                        } else {
                            StudentList(
                                true,
                                students.filter { user -> user.id !in group!!.users.map { it.id } }) { user ->
                                // add invigilator
                                group?.id?.let { groupId ->
                                    user.id?.let {
                                        viewModel.addStudentToGroup(
                                            groupId = groupId,
                                            it
                                        )
                                    }
                                }
                                sheetVisible = false
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }

        }
    }
}

