package com.application.myalarm.mission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentOrange = Color(0xFFFF8C00)

@Composable
fun CameraPermissionRationaleScreen(
    missionName: String,
    onGrantClick: () -> Unit,
    onFallbackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = AccentOrange
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = com.application.myalarm.util.Localizer.t("Camera Access Required"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = com.application.myalarm.util.Localizer.t("The {mission} mission requires camera access to scan codes or capture images. Please grant permission to continue.")
                        .replace("{mission}", com.application.myalarm.util.Localizer.t(missionName)),
                    fontSize = 14.sp,
                    color = Color(0xFFBDBDBD),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onGrantClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                ) {
                    Text(
                        text = com.application.myalarm.util.Localizer.t("Grant Permission"),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onFallbackClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF757575))
                ) {
                    Text(
                        text = com.application.myalarm.util.Localizer.t("Skip Mission (Complete Anyway)"),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
