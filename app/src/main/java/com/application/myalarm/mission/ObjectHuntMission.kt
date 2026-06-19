package com.application.myalarm.mission

import android.Manifest
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.application.myalarm.util.Localizer

private val AccentOrange = Color(0xFFFF8C00)

private val targetObjects = listOf(
    "kettle",
    "toothbrush",
    "remote control",
    "coffee mug",
    "book",
    "shoes",
    "water bottle",
    "plant"
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ObjectHuntMission(onMissionComplete: () -> Unit) {
    val targetObject = remember { targetObjects.random() }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        CameraCaptureScreen(
            title = Localizer.t("Object Hunt"),
            instruction = Localizer.t("Find your {target} and take a photo of it")
                .replace("{target}", Localizer.t(targetObject)),
            iconImageVector = Icons.Default.Search,
            onMissionComplete = onMissionComplete
        )
    } else {
        CameraPermissionRationaleScreen(
            missionName = "Object Hunt",
            onGrantClick = {
                com.application.myalarm.alarm.AlarmActivity.isRequestingPermission = true
                cameraPermissionState.launchPermissionRequest()
            },
            onFallbackClick = onMissionComplete
        )
    }
}
