package com.blink.blinkid.commons

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestNotificationPerm(isGranted: (Boolean) -> Unit) = rememberPermissionState(
    Manifest.permission.POST_NOTIFICATIONS
) { isGranted(it) }

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestReadStoragePerm(isGranted: (Boolean) -> Unit) = rememberPermissionState(
    Manifest.permission.READ_EXTERNAL_STORAGE
) { isGranted(it) }

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestCameraPerm(isGranted: (Boolean) -> Unit) = rememberPermissionState(
    Manifest.permission.CAMERA
) { isGranted(it) }