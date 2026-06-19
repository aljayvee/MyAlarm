package com.application.myalarm.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.data.db.MissionHistoryEntity
import com.application.myalarm.data.repository.DayResult
import com.application.myalarm.data.repository.DayResultType
import com.application.myalarm.data.repository.LifetimeStats
import com.application.myalarm.data.repository.MissionBreakdownItem
import com.application.myalarm.mission.MissionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeAccent = Color(0xFFFFA726)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF4CAF50)
private val SkippedRed = Color(0xFFE53935)

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = viewModel()
) {
    val currentStreak by viewModel.currentStreak.collectAsState()
    val lifetimeStats by viewModel.lifetimeStats.collectAsState()
    val weeklyHeatmap by viewModel.weeklyHeatmap.collectAsState()
    val missionBreakdown by viewModel.missionBreakdown.collectAsState()
    val recentMissions by viewModel.recentMissions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = Localizer.t("Insights"),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        }

        item {
            StreakCard(currentStreak)
        }

        item {
            LifetimeStatsCard(lifetimeStats)
        }

        item {
            HeatmapCard(weeklyHeatmap)
        }

        if (missionBreakdown.isNotEmpty()) {
            item {
                SectionHeader(Localizer.t("By mission"))
            }
            item {
                MissionBreakdownCard(missionBreakdown)
            }
        }

        if (recentMissions.isNotEmpty()) {
            item {
                SectionHeader(Localizer.t("Recent missions"))
            }
            items(recentMissions) { history ->
                RecentMissionCard(history)
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun StreakCard(streak: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(OrangePrimary, OrangeAccent)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localizer.t("Current streak"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (streak == 1) Localizer.t("1 day") else Localizer.t("{streak} days").replace("{streak}", streak.toString()),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun LifetimeStatsCard(stats: LifetimeStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                StatCell(
                    value = "${stats.bestStreak}",
                    label = Localizer.t("Best streak"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = "${stats.dismisses}",
                    label = Localizer.t("Dismisses"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = String.format("%.0f%%", stats.successRate * 100),
                    label = Localizer.t("Success rate"),
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color(0xFFECEFF1)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                val avgSecs = if (stats.avgTimeMs != null && stats.avgTimeMs > 0) {
                    "${stats.avgTimeMs / 1000}s"
                } else {
                    "—"
                }
                StatCell(
                    value = avgSecs,
                    label = Localizer.t("Avg time"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = "${stats.skipped}",
                    label = Localizer.t("Skipped"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = "${stats.totalRuns}",
                    label = Localizer.t("Total runs"),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCell(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = SubtitleGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HeatmapCard(weeklyHeatmap: List<DayResult>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = Localizer.t("Last 4 weeks"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chunk daily history into weeks of 7 days
            val weeks = remember(weeklyHeatmap) {
                weeklyHeatmap.chunked(7).take(4)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { dayResult ->
                            val cellColor = when (dayResult.result) {
                                DayResultType.SUCCESS -> OrangeAccent
                                DayResultType.SKIPPED -> Color(0xFFEF5350)
                                DayResultType.NO_RUN -> Color(0xFFEEEEEE)
                            }
                            val isToday = dayResult.date == LocalDate.now()
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(cellColor)
                                    .then(
                                        if (isToday) Modifier.background(
                                            OrangePrimary,
                                            RoundedCornerShape(6.dp)
                                        ) else Modifier
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = OrangeAccent, label = Localizer.t("Success"))
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(color = Color(0xFFEF5350), label = Localizer.t("Skipped"))
                Spacer(modifier = Modifier.width(16.dp))
                LegendItem(color = Color(0xFFEEEEEE), label = Localizer.t("No run"))
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 12.sp, color = SubtitleGray)
    }
}

@Composable
private fun MissionBreakdownCard(breakdown: List<MissionBreakdownItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            breakdown.forEachIndexed { index, item ->
                val displayName = try {
                    Localizer.t(MissionType.valueOf(item.missionType).displayName)
                } catch (e: Exception) {
                    Localizer.t(item.missionType)
                }

                val rate = if (item.totalAttempts > 0) {
                    (item.successCount.toFloat() / item.totalAttempts.toFloat() * 100).toInt()
                } else {
                    0
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${item.successCount}/${item.totalAttempts}",
                        fontSize = 14.sp,
                        color = SubtitleGray,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                if (rate >= 80) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$rate%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (rate >= 80) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }

                if (index < breakdown.lastIndex) {
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                }
            }
        }
    }
}

@Composable
private fun RecentMissionCard(history: MissionHistoryEntity) {
    val missionName = remember(history.missionType, Localizer.currentLanguage) {
        try {
            Localizer.t(MissionType.valueOf(history.missionType).displayName)
        } catch (e: Exception) {
            Localizer.t(history.missionType)
        }
    }

    val formattedDate = remember(history.completedAt) {
        val date = java.time.Instant.ofEpochMilli(history.completedAt)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime()
        date.format(DateTimeFormatter.ofPattern("MMM dd, hh:mm a"))
    }

    val isSuccess = history.result == "SUCCESS"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = missionName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = SubtitleGray
                )
            }

            val resultText = when (history.result) {
                "SUCCESS" -> Localizer.t("Success")
                "SKIPPED" -> Localizer.t("Skipped")
                else -> Localizer.t(history.result)
            }

            Box(
                modifier = Modifier
                    .background(
                        if (isSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = resultText.lowercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DarkText,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
    )
}
