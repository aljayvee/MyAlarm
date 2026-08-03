package com.application.myalarm.ui.insights

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.time.Instant
import java.time.ZoneId
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.LocalTime
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
private val BlueSnooze = Color(0xFF1E88E5)

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = viewModel()
) {
    val currentStreak by viewModel.currentStreak.collectAsState()
    val lifetimeStats by viewModel.lifetimeStats.collectAsState()
    val weeklyHeatmap by viewModel.weeklyHeatmap.collectAsState()
    val missionBreakdown by viewModel.missionBreakdown.collectAsState()
    val recentMissions by viewModel.recentMissions.collectAsState()
    val snoozeRangeStats by viewModel.snoozeRangeStats.collectAsState()

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
            SnoozeAnalyticsCard(
                rangeStats = snoozeRangeStats,
                onDateRangeSelected = { startMs, endMs ->
                    viewModel.updateSnoozeDateRange(startMs, endMs)
                }
            )
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
                    label = Localizer.t("Total Dismissed"),
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
                    label = Localizer.t("Avg solve time"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = "${stats.totalRuns}",
                    label = Localizer.t("Total runs"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = "${stats.totalSnoozed}",
                    label = Localizer.t("Total snoozed"),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SnoozeAnalyticsCard(
    rangeStats: com.application.myalarm.data.repository.SnoozeRangeStats?,
    onDateRangeSelected: (startMs: Long, endMs: Long) -> Unit
) {
    var selectedPresetLabel by remember { mutableStateOf("Last 7 Days") }
    var showCustomCalendarModal by remember { mutableStateOf(false) }

    val dateRangeDisplay = remember(rangeStats) {
        if (rangeStats != null && rangeStats.startMs > 0L && rangeStats.endMs > 0L) {
            try {
                val startDate = Instant.ofEpochMilli(rangeStats.startMs).atZone(ZoneId.systemDefault()).toLocalDate()
                val endDate = Instant.ofEpochMilli(rangeStats.endMs).atZone(ZoneId.systemDefault()).toLocalDate()
                val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
                "${startDate.format(formatter)} - ${endDate.format(formatter)}"
            } catch (e: Exception) {
                Localizer.t("Last 7 Days")
            }
        } else {
            Localizer.t("Last 7 Days")
        }
    }

    if (showCustomCalendarModal) {
        CustomCalendarRangeModal(
            onDismiss = { showCustomCalendarModal = false },
            onRangeSelected = { startMs, endMs, label ->
                selectedPresetLabel = label
                onDateRangeSelected(startMs, endMs)
                showCustomCalendarModal = false
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Snooze,
                        contentDescription = "Snooze Analytics",
                        tint = BlueSnooze,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localizer.t("Snooze Analytics"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }

                // Date Range Selector Pill
                Surface(
                    onClick = { showCustomCalendarModal = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.height(32.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = DarkText,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Localizer.t(selectedPresetLabel),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = DarkText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dateRangeDisplay,
                fontSize = 11.sp,
                color = SubtitleGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            val totalSnoozed = rangeStats?.totalSnoozed ?: 0
            val avgPerDay = rangeStats?.avgSnoozePerDay ?: 0f
            val daysCount = rangeStats?.daysInRange ?: 1

            Row(modifier = Modifier.fillMaxWidth()) {
                StatCell(
                    value = "$totalSnoozed",
                    label = Localizer.t("Snoozed in Range"),
                    modifier = Modifier.weight(1f)
                )
                StatCell(
                    value = String.format("%.1f", avgPerDay),
                    label = Localizer.t("Daily Avg ($daysCount d)"),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CustomCalendarRangeModal(
    onDismiss: () -> Unit,
    onRangeSelected: (startMs: Long, endMs: Long, label: String) -> Unit
) {
    var selectedPreset by remember { mutableStateOf("This month") }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    var startDate by remember {
        mutableStateOf<LocalDate?>(LocalDate.now().withDayOfMonth(1))
    }
    var endDate by remember {
        mutableStateOf<LocalDate?>(LocalDate.now())
    }

    fun applyPreset(preset: String) {
        selectedPreset = preset
        val today = LocalDate.now()
        when (preset) {
            "Today" -> {
                startDate = today
                endDate = today
            }
            "Last 7 days" -> {
                startDate = today.minusDays(6)
                endDate = today
            }
            "This week" -> {
                startDate = today.with(DayOfWeek.MONDAY)
                endDate = today
            }
            "Last month" -> {
                val lastMonth = today.minusMonths(1)
                startDate = lastMonth.withDayOfMonth(1)
                endDate = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
                currentYearMonth = YearMonth.from(lastMonth)
            }
            "This month" -> {
                startDate = today.withDayOfMonth(1)
                endDate = today
                currentYearMonth = YearMonth.from(today)
            }
            "Custom range" -> {}
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF0F172A), // Dark slate blue background
            border = BorderStroke(1.dp, Color(0xFF1E293B)),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Top Row: Close Button (✕)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), CircleShape)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Preset Chips (2 Rows matching the reference design)
                val row1 = listOf("Today", "Last 7 days", "Last month")
                val row2 = listOf("This month", "This week", "Custom range")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row1.forEach { preset ->
                        PresetChip(
                            label = preset,
                            isSelected = selectedPreset == preset,
                            onClick = { applyPreset(preset) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row2.forEach { preset ->
                        PresetChip(
                            label = preset,
                            isSelected = selectedPreset == preset,
                            onClick = { applyPreset(preset) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = Color(0xFF1E293B))

                Spacer(modifier = Modifier.height(16.dp))

                // Month Navigation Header (< Month Year >)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.4f)), CircleShape)
                            .clickable { currentYearMonth = currentYearMonth.minusMonths(1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Month",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.4f)), CircleShape)
                            .clickable { currentYearMonth = currentYearMonth.plusMonths(1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Month",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of Week Header (Su, Mo, Tu, We, Th, Fr, Sa)
                val dayHeaders = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    dayHeaders.forEach { header ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = header,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Calendar Grid with smooth month glide transition
                AnimatedContent(
                    targetState = currentYearMonth,
                    transitionSpec = {
                        if (targetState.isAfter(initialState)) {
                            (slideInHorizontally { width -> width / 3 } + fadeIn(tween(250))).togetherWith(
                                slideOutHorizontally { width -> -width / 3 } + fadeOut(tween(200))
                            )
                        } else {
                            (slideInHorizontally { width -> -width / 3 } + fadeIn(tween(250))).togetherWith(
                                slideOutHorizontally { width -> width / 3 } + fadeOut(tween(200))
                            )
                        }
                    },
                    label = "monthTransition"
                ) { targetMonth ->
                    CalendarMonthGrid(
                        yearMonth = targetMonth,
                        startDate = startDate,
                        endDate = endDate,
                        onDateClick = { clickedDate ->
                            selectedPreset = "Custom range"
                            if (startDate == null || (startDate != null && endDate != null)) {
                                startDate = clickedDate
                                endDate = null
                            } else if (startDate != null && endDate == null) {
                                if (clickedDate.isBefore(startDate)) {
                                    startDate = clickedDate
                                } else {
                                    endDate = clickedDate
                                }
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Right Done Button (Yellow Pill)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            val s = startDate ?: LocalDate.now()
                            val e = endDate ?: s
                            val startMs = s.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val endMs = e.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            onRangeSelected(startMs, endMs, selectedPreset)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC107),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(24.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = Localizer.t("Done"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipBg by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFFC107) else Color(0xFF1E293B),
        animationSpec = tween(200),
        label = "chipBg"
    )
    val chipText by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.White.copy(alpha = 0.85f),
        animationSpec = tween(200),
        label = "chipText"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = chipBg,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.4f)),
        modifier = modifier.height(36.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = Localizer.t(label),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = chipText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    yearMonth: YearMonth,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0
    val daysInMonth = yearMonth.lengthOfMonth()

    val totalCells = firstDayOfWeek + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (r in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (c in 0 until 7) {
                    val dayIndex = r * 7 + c
                    val dayNumber = dayIndex - firstDayOfWeek + 1

                    if (dayNumber in 1..daysInMonth) {
                        val date = yearMonth.atDay(dayNumber)
                        val isStart = startDate != null && date == startDate
                        val isEnd = endDate != null && date == endDate
                        val isInRange = startDate != null && endDate != null &&
                                (date.isAfter(startDate) && date.isBefore(endDate))
                        val isToday = date == LocalDate.now()

                        val targetBg = when {
                            isStart || isEnd -> Color(0xFFFFC107)
                            isInRange -> Color(0xFF3B2E0A)
                            else -> Color.Transparent
                        }

                        val targetTextColor = when {
                            isStart || isEnd -> Color.Black
                            isInRange -> Color(0xFFFFD54F)
                            else -> Color.White
                        }

                        val animCellBg by animateColorAsState(
                            targetValue = targetBg,
                            animationSpec = tween(250),
                            label = "animCellBg"
                        )

                        val animTextColor by animateColorAsState(
                            targetValue = targetTextColor,
                            animationSpec = tween(250),
                            label = "animTextColor"
                        )

                        val shape = when {
                            isStart && isEnd -> CircleShape
                            isStart -> if (c == 6) CircleShape else RoundedCornerShape(topStart = 19.dp, bottomStart = 19.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                            isEnd -> if (c == 0) CircleShape else RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 19.dp, bottomEnd = 19.dp)
                            isInRange -> when (c) {
                                0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp, topEnd = 0.dp, bottomEnd = 0.dp)
                                6 -> RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp)
                                else -> RoundedCornerShape(0.dp)
                            }
                            else -> CircleShape
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(shape)
                                .background(animCellBg)
                                .then(
                                    if (isToday && !isStart && !isEnd) Modifier.border(
                                        width = 1.5.dp,
                                        color = Color(0xFFFFC107),
                                        shape = CircleShape
                                    ) else Modifier
                                )
                                .clickable { onDateClick(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$dayNumber",
                                fontSize = 13.sp,
                                fontWeight = if (isStart || isEnd || isToday) FontWeight.Bold else FontWeight.Normal,
                                color = animTextColor
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        )
                    }
                }
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
                LegendItem(color = Color(0xFFEF5350), label = Localizer.t("Dismissed"))
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

    val isSuccess = history.result == "SUCCESS" || history.result == "DISMISSED"

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
                "SKIPPED" -> Localizer.t("Dismissed")
                "DISMISSED" -> Localizer.t("Dismissed")
                "SNOOZED" -> Localizer.t("Snoozed")
                else -> Localizer.t(history.result)
            }

            Box(
                modifier = Modifier
                    .background(
                        if (isSuccess) Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = resultText.lowercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFF1565C0)
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
