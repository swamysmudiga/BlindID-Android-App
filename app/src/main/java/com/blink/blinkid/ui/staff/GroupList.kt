package com.blink.blinkid.ui.staff

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
import com.blink.blinkid.commons.NetworkResult
import com.blink.blinkid.model.Group
import com.blink.blinkid.ui.teacher.HeaderText
import com.blink.blinkid.viewmodel.GroupViewModel

@Composable
fun GroupListScreen(
    navController: NavController,
    groupViewModel: GroupViewModel,
) {
    var groupList by remember { mutableStateOf<List<Group>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val result by groupViewModel.groups.collectAsState(NetworkResult.Initial)

    LaunchedEffect(true) {
        groupViewModel.getGroups()
    }
    when (result) {
        is NetworkResult.Success -> {
            isLoading = false
            Log.e(
                "GroupListScreen",
                "GroupListScreen: ${result as NetworkResult.Success<List<Group>>}"
            )
            groupList = (result as NetworkResult.Success<List<Group>>).body!!
        }

        is NetworkResult.Error -> {
            isLoading = false

            Log.e(
                "GroupListScreen",
                "GroupListScreen: ${result as NetworkResult.Error<List<Group>>}"
            )
        }

        is NetworkResult.Loading -> {
            Log.e("GroupListScreen", "GroupListScreen: loading...")
            isLoading = true
        }

        else -> {
            Log.e("GroupListScreen", "GroupListScreen: else...")
        }
    }

    Column {
        HeaderText("Groups")


        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color(0xFFF8F8F8))
        ) {

            if (isLoading) {
                ProgressBar()
            } else {
                if (groupList.isEmpty()) {
                    Text(
                        text = "No groups available",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                } else {
                    GroupList(groupList) {
                        groupViewModel.setSelectedGroup(it)
                        navController.navigate(Navigation.Routes.GROUP_DETAIL + "/${it.id}")
                    }
                }
            }
        }
    }

}


@Composable
fun GroupList(groups: List<Group>, onUserClick: (Group) -> Unit) {
    Log.e("GroupList", "GroupList: $groups")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        groups.forEach { group ->
            GroupCard(group, onUserClick)
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
fun GroupCard(group: Group, onUserClick: (Group) -> Unit) {

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .graphicsLayer(shadowElevation = 2.0f)
            .padding(12.dp)
            .clickable { onUserClick(group) }
    ) {

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = group.name, fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }

        Text(
            text = group.description,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            if (group.admins.isNotEmpty())
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Staff",
                        color = Color.Gray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = group.admins[0].username,
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            Spacer(modifier = Modifier.weight(1f))

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
