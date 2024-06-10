package com.blink.blinkid.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.blink.blinkid.model.User


@Composable
fun StudentList(
    isSearching: Boolean,
    list: List<User>,
    onUserClick: (User) -> Unit
) {

    var searchText by remember { mutableStateOf("") }

    Column {
        if (isSearching)
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            )

        LazyColumn {
            val filteredList = list.filter {
                it.username.contains(searchText, ignoreCase = true)
            }

            items(filteredList.size) {
                UserCard(user = filteredList[it]) { user ->
                    onUserClick(user)
                }
            }
        }
    }
}


@Composable
fun UserCard(user: User, onUserClick: (User) -> Unit) {

    Row(modifier = Modifier
        .background(Color.White, RoundedCornerShape(15.dp))
        .graphicsLayer(shadowElevation = 2.0f)
        .padding(12.dp)
        .clickable {
            onUserClick(user)
        }
    ) {
        if (user.images.isNotEmpty())
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.images[0].url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Image of ${user.username}",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .graphicsLayer(shadowElevation = 2.0f)
            )
        if (user.images.isNotEmpty())
            Spacer(modifier = Modifier.width(10.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = user.username, fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = user.id.toString(), fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = user.email,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun UserCardWithDelete(user: User, deleteUser: (Int) -> Unit, onUserClick: (User) -> Unit) {

    val confirmationDialogState = rememberConfirmationDialogState()

    Row(modifier = Modifier
        .background(Color.White, RoundedCornerShape(15.dp))
        .graphicsLayer(shadowElevation = 2.0f)
        .padding(12.dp)
        .clickable {
            onUserClick(user)
        }
    ) {
        if (user.images.isNotEmpty())
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.images[0].url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Image of ${user.username}",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .padding(0.dp)
                    .clip(CircleShape)
                    .graphicsLayer(shadowElevation = 2.0f)
            )
        if (user.images.isNotEmpty())
            Spacer(modifier = Modifier.width(10.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = user.username, fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = user.id.toString(), fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))


            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = user.email,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Delete",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        user.id?.let {
                            confirmationDialogState.showConfirmationDialog()
                        }
                    }
                )
            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    if (confirmationDialogState.value) {
        ConfirmationDialog(
            dialogState = confirmationDialogState,
            onConfirmClick = {
                user.id?.let {
                    deleteUser(it)
                }
            },
            title = "Delete User",
            text = "Are you sure you want to delete ${user.username}?"
        )
    }
}

@Composable
fun ConfirmationDialog(
    dialogState: ConfirmationDialogState,
    onConfirmClick: () -> Unit,
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { dialogState.dismissDialog() },
        title = { Text(title) },
        text = { Text(text) },
        modifier = modifier,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmClick()
                    dialogState.dismissDialog()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dialogState.dismissDialog() }
            ) {
                Text("Cancel")
            }
        }
    )
}

class ConfirmationDialogState(
    private val initialValue: Boolean = false
) : State<Boolean> {
    private val data = mutableStateOf(initialValue)
    override val value: Boolean get() = data.value

    fun showConfirmationDialog() {
        data.value = true
    }

    fun dismissDialog() {
        data.value = false
    }
}

@Composable
fun rememberConfirmationDialogState(
    initialValue: Boolean = false
) = remember {
    ConfirmationDialogState(initialValue)
}