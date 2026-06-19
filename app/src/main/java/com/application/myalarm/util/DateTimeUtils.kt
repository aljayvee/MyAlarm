package com.application.myalarm.util

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object DateTimeUtils {

    private val DAY_LETTERS = listOf("M", "T", "W", "T", "F", "S", "S")

    private val BITMASK_TO_DAY_OF_WEEK = mapOf(
        0 to DayOfWeek.MONDAY,
        1 to DayOfWeek.TUESDAY,
        2 to DayOfWeek.WEDNESDAY,
        3 to DayOfWeek.THURSDAY,
        4 to DayOfWeek.FRIDAY,
        5 to DayOfWeek.SATURDAY,
        6 to DayOfWeek.SUNDAY
    )

    fun calculateNextAlarmTime(hour: Int, minute: Int, repeatDays: Int): LocalDateTime {
        val now = LocalDateTime.now()
        val alarmTimeToday = LocalDate.now().atTime(hour, minute)

        if (repeatDays == 0) {
            return if (alarmTimeToday.isAfter(now)) {
                alarmTimeToday
            } else {
                alarmTimeToday.plusDays(1)
            }
        }

        val today = LocalDate.now()
        for (daysAhead in 0..6) {
            val candidateDate = today.plusDays(daysAhead.toLong())
            val candidateDay = candidateDate.dayOfWeek
            val bitIndex = when (candidateDay) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }

            if (repeatDays and (1 shl bitIndex) != 0) {
                val candidateDateTime = candidateDate.atTime(hour, minute)
                if (candidateDateTime.isAfter(now)) {
                    return candidateDateTime
                }
            }
        }

        // Fallback: find the first matching day next week
        for (daysAhead in 7..13) {
            val candidateDate = today.plusDays(daysAhead.toLong())
            val candidateDay = candidateDate.dayOfWeek
            val bitIndex = when (candidateDay) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }

            if (repeatDays and (1 shl bitIndex) != 0) {
                return candidateDate.atTime(hour, minute)
            }
        }

        // Should not happen if repeatDays has at least one bit set
        return alarmTimeToday.plusDays(1)
    }

    fun formatTimeUntil(target: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = Duration.between(now, target)

        if (duration.isNegative) {
            return Localizer.t("Rings now")
        }

        val totalMinutes = duration.toMinutes()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        val timeStr = when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "less than 1m"
        }
        return Localizer.t("Rings in {time}").replace("{time}", timeStr)
    }

    fun formatTime12h(hour: Int, minute: Int): Pair<String, String> {
        val amPm = if (hour < 12) "am" else "pm"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val timeString = "$displayHour:${minute.toString().padStart(2, '0')}"
        return Pair(timeString, amPm)
    }

    fun getWeekDates(): List<LocalDate> {
        val today = LocalDate.now()
        val sunday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        return (0..6).map { sunday.plusDays(it.toLong()) }
    }

    fun formatRepeatDays(repeatDays: Int): String {
        if (repeatDays == 0) return ""

        val allBitsSet = 0b1111111
        if (repeatDays and allBitsSet == allBitsSet) return Localizer.t("Every day")

        val weekdayBits = 0b0011111
        if (repeatDays and weekdayBits == weekdayBits && repeatDays and 0b1100000 == 0) {
            return Localizer.t("Weekdays")
        }

        val weekendBits = 0b1100000
        if (repeatDays and weekendBits == weekendBits && repeatDays and 0b0011111 == 0) {
            return Localizer.t("Weekends")
        }

        val initials = listOf(
            Localizer.t("Day_Mon_Initial"),
            Localizer.t("Day_Tue_Initial"),
            Localizer.t("Day_Wed_Initial"),
            Localizer.t("Day_Thu_Initial"),
            Localizer.t("Day_Fri_Initial"),
            Localizer.t("Day_Sat_Initial"),
            Localizer.t("Day_Sun_Initial")
        )

        return initials.mapIndexed { index, initial ->
            if (repeatDays and (1 shl index) != 0) initial else null
        }.filterNotNull().joinToString(" · ")
    }

    fun getNextAlarmText(hour: Int, minute: Int, repeatDays: Int): String {
        val nextAlarm = calculateNextAlarmTime(hour, minute, repeatDays)
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val alarmDate = nextAlarm.toLocalDate()

        val timeStr = String.format("%02d:%02d", nextAlarm.hour, nextAlarm.minute)

        return when (alarmDate) {
            today -> Localizer.t("Next: Today {time}").replace("{time}", timeStr)
            tomorrow -> Localizer.t("Next: Tomorrow {time}").replace("{time}", timeStr)
            else -> {
                val rawDayName = alarmDate.dayOfWeek.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                val localizedDay = Localizer.t(rawDayName)
                Localizer.t("Next: {day} {time}")
                    .replace("{day}", localizedDay)
                    .replace("{time}", timeStr)
            }
        }
    }

    fun startOfDay(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun endOfDay(date: LocalDate): Long {
        return date.atTime(LocalTime.MAX)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun startOfToday(): Long {
        return startOfDay(LocalDate.now())
    }
}
