package com.application.myalarm.mission

import android.Manifest
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.application.myalarm.util.Localizer

private val AccentOrange = Color(0xFFFF8C00)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MakeYourBedMission(onMissionComplete: () -> Unit) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        CameraCaptureScreen(
            title = Localizer.t("Make Your Bed"),
            instruction = Localizer.t("Make your bed, then take a photo as proof"),
            iconImageVector = Icons.Default.Bed,
            onMissionComplete = onMissionComplete
        )
    } else {
        CameraPermissionRationaleScreen(
            missionName = "Make Your Bed",
            onGrantClick = {
                com.application.myalarm.alarm.AlarmActivity.isRequestingPermission = true
                cameraPermissionState.launchPermissionRequest()
            },
            onFallbackClick = onMissionComplete
        )
    }
}
