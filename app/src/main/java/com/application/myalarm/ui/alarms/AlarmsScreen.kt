package com.application.myalarm.ui.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.data.db.AlarmEntity
import com.application.myalarm.mission.MissionType
import com.application.myalarm.util.DateTimeUtils
import com.application.myalarm.util.Localizer
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings
import android.content.Intent
import android.net.Uri
import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.BorderStroke
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner

private val OrangePrimary = Color(0xFFFF8C00)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val GreenToggle = Color(0xFF4CAF50)

@Composable
fun AlarmsScreen(
    onNavigate: (String) -> Unit,
    viewModel: AlarmsViewModel = viewModel()
) {
    val alarms by viewModel.alarms.collectAsState()
    val alarmCount by viewModel.alarmCount.collectAsState()

    val context = LocalContext.current

    fun checkNotificationPermission(ctx: android.content.Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    var hasNotificationPermission by remember { mutableStateOf(checkNotificationPermission(context)) }
    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasNotificationPermission = checkNotificationPermission(context)
                hasOverlayPermission = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showNotifRequiredDialog by remember { mutableStateOf(false) }

    if (showNotifRequiredDialog) {
        AlertDialog(
            onDismissRequest = { showNotifRequiredDialog = false },
            title = {
                Text(
                    text = Localizer.t("Notification Permission Required"),
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            },
            text = {
                Text(
                    text = Localizer.t("To run alarms properly, notification access is essential. Please enable it in Settings so MyAlarm can ring."),
                    color = DarkText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotifRequiredDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = OrangePrimary)
                ) {
                    Text(Localizer.t("Open Settings"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showNotifRequiredDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                ) {
                    Text(Localizer.t("Cancel"))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    var showDeleteConfirmDialog by remember { mutableStateOf<Long?>(null) }
    var showOverlayPermissionDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text(Localizer.t("Delete Alarm")) },
            text = { Text(Localizer.t("Are you sure you want to delete this alarm?")) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmDialog?.let { id ->
                            viewModel.deleteAlarm(id)
                        }
                        showDeleteConfirmDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = OrangePrimary)
                ) {
                    Text(Localizer.t("Delete"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                ) {
                    Text(Localizer.t("Cancel"))
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Localizer.t("Alarms"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Text(
                text = if (alarmCount == 1) Localizer.t("1 alarm") else Localizer.t("{count} alarms").replace("{count}", alarmCount.toString()),
                fontSize = 14.sp,
                color = SubtitleGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (!hasNotificationPermission) {
                PermissionWarningCard(
                    title = Localizer.t("Notification Permission Required"),
                    message = Localizer.t("Without notifications enabled, MyAlarm cannot ring or alert you when your alarms trigger. Please enable notifications to run the app properly."),
                    onActionClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!hasOverlayPermission) {
                PermissionWarningCard(
                    title = Localizer.t("Display Over Other Apps Required"),
                    message = Localizer.t("To show the alarm screen and missions over your lock screen or when using other apps, MyAlarm needs permission to draw over other apps."),
                    onActionClick = {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        )
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (alarms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = Localizer.t("No alarms set"),
                            tint = SubtitleGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = Localizer.t("You don't set any alarms yet!"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = SubtitleGray
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                val needOverlay = alarms.isEmpty() && !Settings.canDrawOverlays(context)
                                if (needOverlay) {
                                    showOverlayPermissionDialog = true
                                } else {
                                    onNavigate("alarm_edit/-1")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrangePrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(
                                text = Localizer.t("Create your first alarm"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(alarms, key = { it.id }) { alarm ->
                        AlarmCard(
                            alarm = alarm,
                            onToggle = { enabled -> 
                                if (enabled && !hasNotificationPermission) {
                                    showNotifRequiredDialog = true
                                } else {
                                    viewModel.toggleAlarm(alarm.id, enabled) 
                                }
                            },
                            onDelete = { showDeleteConfirmDialog = alarm.id },
                            onClick = { onNavigate("alarm_edit/${alarm.id}") }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(160.dp))
                    }
                }
            }
        }

        if (showOverlayPermissionDialog) {
            AlertDialog(
                onDismissRequest = {
                    showOverlayPermissionDialog = false
                },
                title = {
                    Text(
                        text = Localizer.t("Display Over Other Apps"),
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                text = {
                    Text(
                        text = Localizer.t("To show the alarm screen and missions over your lock screen or when using other apps, MyAlarm needs permission to draw over other apps. Please enable this setting on the next screen."),
                        color = DarkText,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showOverlayPermissionDialog = false
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:${context.packageName}")
                            )
                            context.startActivity(intent)
                            onNavigate("alarm_edit/-1")
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = OrangePrimary)
                    ) {
                        Text(Localizer.t("Grant"))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showOverlayPermissionDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                    ) {
                        Text(Localizer.t("Cancel"))
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Floating add button container at the bottom
        if (alarms.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 92.dp, top = 20.dp)
            ) {
                Button(
                    onClick = {
                        val needOverlay = alarms.isEmpty() && !Settings.canDrawOverlays(context)
                        if (needOverlay) {
                            showOverlayPermissionDialog = true
                        } else {
                            onNavigate("alarm_edit/-1")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkText,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = Localizer.t("+ New alarm"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AlarmCard(
    alarm: AlarmEntity,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val (timeString, amPm) = remember(alarm.hour, alarm.minute) {
        DateTimeUtils.formatTime12h(alarm.hour, alarm.minute)
    }

    val repeatText = remember(alarm.repeatDays, Localizer.currentLanguage) {
        DateTimeUtils.formatRepeatDays(alarm.repeatDays)
    }

    val missionName = remember(alarm.missionType, Localizer.currentLanguage) {
        try {
            Localizer.t(MissionType.valueOf(alarm.missionType).displayName)
        } catch (e: Exception) {
            Localizer.t("Math Problem")
        }
    }

    val nextRingText = remember(alarm.hour, alarm.minute, alarm.repeatDays, alarm.isEnabled, Localizer.currentLanguage) {
        if (alarm.isEnabled) {
            DateTimeUtils.getNextAlarmText(alarm.hour, alarm.minute, alarm.repeatDays)
        } else {
            ""
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = timeString,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (alarm.isEnabled) DarkText else SubtitleGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = amPm,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SubtitleGray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$missionName · $repeatText",
                    fontSize = 12.sp,
                    color = SubtitleGray
                )

                if (nextRingText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = nextRingText,
                        fontSize = 12.sp,
                        color = OrangePrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = Localizer.t("Delete"),
                        tint = SubtitleGray
                    )
                }

                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GreenToggle,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE0E0E0),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
private fun PermissionWarningCard(
    title: String,
    message: String,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEEBEE)),
        border = BorderStroke(1.dp, Color(0xFFEF9A9A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                fontSize = 12.sp,
                color = Color(0xFFC62828),
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.End).height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text(text = Localizer.t("Fix in Settings"), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
