package com.application.myalarm.ui.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.data.db.MissionHistoryEntity
import com.application.myalarm.data.repository.DayResult
import com.application.myalarm.data.repository.DayResultType
import com.application.myalarm.mission.MissionType
import com.application.myalarm.util.DateTimeUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeAccent = Color(0xFFFFA726)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val GreenToggle = Color(0xFF4CAF50)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val nextAlarm by viewModel.nextAlarm.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()
    val weeklyCalendar by viewModel.weeklyCalendar.collectAsState()
    val todayMissions by viewModel.todayMissions.collectAsState()
    val timeUntilAlarm by viewModel.timeUntilAlarm.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderRow(currentStreak)
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            WeeklyCalendarCard(weeklyCalendar)
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            NextAlarmCard(
                alarm = nextAlarm,
                timeUntil = timeUntilAlarm,
                onToggle = { id, enabled -> viewModel.toggleAlarm(id, enabled) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            TodayWakeUpHeader(onHistoryClick = { onNavigate("insights") })
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (todayMissions.isEmpty()) {
            item {
                EmptyTodayCard()
            }
        } else {
            items(todayMissions) { mission ->
                TodayMissionCard(mission, todayMissions.indexOf(mission) + 1)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun HeaderRow(currentStreak: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Alarm",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Box(
            modifier = Modifier
                .background(OrangeLight, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🔥", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$currentStreak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangePrimary
                )
            }
        }
    }
}

@Composable
private fun WeeklyCalendarCard(weeklyCalendar: List<DayResult>) {
    val today = LocalDate.now()
    val weekDates = if (weeklyCalendar.isNotEmpty()) {
        weeklyCalendar.map { it.date }
    } else {
        DateTimeUtils.getWeekDates()
    }

    val dayLabels = listOf(
        Localizer.t("Day_Sun_Initial"),
        Localizer.t("Day_Mon_Initial"),
        Localizer.t("Day_Tue_Initial"),
        Localizer.t("Day_Wed_Initial"),
        Localizer.t("Day_Thu_Initial"),
        Localizer.t("Day_Fri_Initial"),
        Localizer.t("Day_Sat_Initial")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDates.forEachIndexed { index, date ->
                val dayResult = weeklyCalendar.find { it.date == date }
                val isToday = date == today
                val dayOfWeek = date.dayOfWeek.value % 7 // Convert to Sun=0

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayLabels.getOrElse(dayOfWeek) { "?" },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SubtitleGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    dayResult?.result == DayResultType.SUCCESS -> OrangePrimary
                                    isToday -> OrangeLight
                                    else -> Color.Transparent
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayResult?.result == DayResultType.SUCCESS) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "${date.dayOfMonth}",
                                fontSize = 14.sp,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                color = if (isToday) OrangePrimary else DarkText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NextAlarmCard(
    alarm: com.application.myalarm.data.db.AlarmEntity?,
    timeUntil: String,
    onToggle: (Long, Boolean) -> Unit
) {
    if (alarm == null) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Localizer.t("No alarms set"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = SubtitleGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = Localizer.t("Tap the Alarms tab to create one"),
                    fontSize = 14.sp,
                    color = SubtitleGray
                )
            }
        }
        return
    }

    val timePair = DateTimeUtils.formatTime12h(alarm.hour, alarm.minute)
    val missionType = try {
        MissionType.valueOf(alarm.missionType)
    } catch (e: Exception) {
        MissionType.MATH_PROBLEM
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = Localizer.t("Today"),
                fontSize = 14.sp,
                color = SubtitleGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = timePair.first,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = timePair.second,
                        fontSize = 20.sp,
                        color = SubtitleGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggle(alarm.id, it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GreenToggle,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = SubtitleGray.copy(alpha = 0.3f)
                    )
                )
            }

            if (timeUntil.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = SubtitleGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = timeUntil,
                        fontSize = 14.sp,
                        color = SubtitleGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = LightBackground)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(OrangeLight, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = OrangePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = Localizer.t(missionType.displayName),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText
                    )
                    Text(
                        text = Localizer.t("Mission"),
                        fontSize = 12.sp,
                        color = SubtitleGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = LightBackground)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(OrangeLight, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = OrangePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = Localizer.t(alarm.soundName),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText
                    )
                    Text(
                        text = Localizer.t("Sound"),
                        fontSize = 12.sp,
                        color = SubtitleGray
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayWakeUpHeader(onHistoryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = Localizer.t("Today's Wake Up"),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = Localizer.t("History"),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = OrangePrimary,
            modifier = Modifier.clickable { onHistoryClick() }
        )
    }
}

@Composable
private fun TodayMissionCard(mission: MissionHistoryEntity, sequenceNumber: Int) {
    val missionType = try {
        MissionType.valueOf(mission.missionType)
    } catch (e: Exception) {
        MissionType.MATH_PROBLEM
    }

    val dateFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    val completedTime = java.time.Instant.ofEpochMilli(mission.completedAt)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalTime()
    val timeString = completedTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    val dateString = java.time.Instant.ofEpochMilli(mission.completedAt)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("MMM d"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(OrangeLight, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = OrangePrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = timeString.lowercase(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                    Text(
                        text = "  ·  ${Localizer.t(missionType.displayName)}",
                        fontSize = 13.sp,
                        color = SubtitleGray
                    )
                }
                Text(
                    text = dateString,
                    fontSize = 12.sp,
                    color = SubtitleGray
                )
            }

            Box(
                modifier = Modifier
                    .background(LightBackground, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "#$sequenceNumber",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SubtitleGray
                )
            }
        }
    }
}

@Composable
private fun EmptyTodayCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "😴",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = Localizer.t("No wake-ups today yet"),
                fontSize = 15.sp,
                color = SubtitleGray
            )
        }
    }
}
