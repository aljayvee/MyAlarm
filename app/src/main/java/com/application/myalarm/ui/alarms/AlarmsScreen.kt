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

    var showDeleteConfirmDialog by remember { mutableStateOf<Long?>(null) }

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
                            text = Localizer.t("No alarms set"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = SubtitleGray
                        )
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
                            onToggle = { enabled -> viewModel.toggleAlarm(alarm.id, enabled) },
                            onDelete = { showDeleteConfirmDialog = alarm.id },
                            onClick = { onNavigate("alarm_edit/${alarm.id}") }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }

        // Floating add button container at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = { onNavigate("alarm_edit/-1") },
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
