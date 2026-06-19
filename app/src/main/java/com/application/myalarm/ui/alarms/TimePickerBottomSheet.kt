package com.application.myalarm.ui.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.abs

private val OrangePrimary = Color(0xFFFF8C00)
private val DarkText = Color(0xFF2D2D2D)
private val LightBackground = Color(0xFFF5F5F5)
private val SubtitleGray = Color(0xFF9E9E9E)

private const val VISIBLE_ITEMS = 5
private const val ITEM_HEIGHT_DP = 48
private const val BUFFER_SIZE = 1000

@Composable
fun TimePickerBottomSheet(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val initialDisplay12 = if (initialHour == 0) 12
    else if (initialHour > 12) initialHour - 12
    else initialHour
    val initialIsAm = initialHour < 12

    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()
    val amPmListState = rememberLazyListState()

    val hours = (1..12).toList()
    val minutes = (0..59).toList()
    val amPmOptions = listOf("AM", "PM")

    val hourCenter = BUFFER_SIZE / 2 - (BUFFER_SIZE / 2 % 12) + (initialDisplay12 - 1)
    val minuteCenter = BUFFER_SIZE / 2 - (BUFFER_SIZE / 2 % 60) + initialMinute
    val amPmInitial = if (initialIsAm) 0 else 1
    val hourFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        hourListState.scrollToItem(hourCenter - VISIBLE_ITEMS / 2)
        minuteListState.scrollToItem(minuteCenter - VISIBLE_ITEMS / 2)
        amPmListState.scrollToItem(amPmInitial)
        try {
            hourFocusRequester.requestFocus()
        } catch (e: Exception) {
            // Ignore
        }
    }

    val selectedHourIndex by remember {
        derivedStateOf {
            val layoutInfo = hourListState.layoutInfo
            val center = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }?.index ?: hourCenter
        }
    }

    val selectedMinuteIndex by remember {
        derivedStateOf {
            val layoutInfo = minuteListState.layoutInfo
            val center = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }?.index ?: minuteCenter
        }
    }

    val selectedAmPmIndex by remember {
        derivedStateOf {
            val layoutInfo = amPmListState.layoutInfo
            val center = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            val closestItem = layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }
            if (closestItem != null) {
                val pad = VISIBLE_ITEMS / 2
                val optIndex = closestItem.index - pad
                optIndex.coerceIn(0, amPmOptions.lastIndex)
            } else {
                amPmInitial
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Cancel",
                        color = OrangePrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Set alarm time",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(ITEM_HEIGHT_DP.dp)
                            .background(
                                LightBackground,
                                RoundedCornerShape(12.dp)
                            )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WheelColumn(
                            listState = hourListState,
                            itemCount = BUFFER_SIZE,
                            selectedIndex = selectedHourIndex,
                            getLabel = { index -> hours[index % 12].toString() },
                            modifier = Modifier
                                .width(80.dp)
                                .focusRequester(hourFocusRequester)
                                .focusable()
                        )

                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        WheelColumn(
                            listState = minuteListState,
                            itemCount = BUFFER_SIZE,
                            selectedIndex = selectedMinuteIndex,
                            getLabel = { index ->
                                String.format("%02d", minutes[index % 60])
                            },
                            modifier = Modifier.width(80.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        AmPmWheelColumn(
                            listState = amPmListState,
                            options = amPmOptions,
                            selectedIndex = selectedAmPmIndex,
                            modifier = Modifier.width(72.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val selectedHour12 = hours[selectedHourIndex % 12]
                        val selectedMin = minutes[selectedMinuteIndex % 60]
                        val isAm = selectedAmPmIndex == 0

                        val hour24 = when {
                            isAm && selectedHour12 == 12 -> 0
                            !isAm && selectedHour12 == 12 -> 12
                            !isAm -> selectedHour12 + 12
                            else -> selectedHour12
                        }
                        onTimeSelected(hour24, selectedMin)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkText
                    )
                ) {
                    Text(
                        text = "✓ Set",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun WheelColumn(
    listState: androidx.compose.foundation.lazy.LazyListState,
    itemCount: Int,
    selectedIndex: Int,
    getLabel: (Int) -> String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier.height((ITEM_HEIGHT_DP * VISIBLE_ITEMS).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = androidx.compose.foundation.gestures.ScrollableDefaults.flingBehavior()
    ) {
        items(itemCount) { index ->
            val isSelected = index == selectedIndex
            val distanceFromCenter = abs(index - selectedIndex)
            val alpha = when {
                isSelected -> 1f
                distanceFromCenter == 1 -> 0.5f
                distanceFromCenter == 2 -> 0.25f
                else -> 0.15f
            }
            val fontSize = if (isSelected) 22.sp else 16.sp

            Box(
                modifier = Modifier
                    .height(ITEM_HEIGHT_DP.dp)
                    .fillMaxWidth()
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getLabel(index),
                    fontSize = fontSize,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = DarkText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AmPmWheelColumn(
    listState: androidx.compose.foundation.lazy.LazyListState,
    options: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    val pad = VISIBLE_ITEMS / 2
    val totalItems = options.size + pad * 2

    LazyColumn(
        state = listState,
        modifier = modifier.height((ITEM_HEIGHT_DP * VISIBLE_ITEMS).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = androidx.compose.foundation.gestures.ScrollableDefaults.flingBehavior()
    ) {
        items(totalItems) { index ->
            val isSpacer = index < pad || index >= pad + options.size
            val optIndex = index - pad
            val isSelected = !isSpacer && optIndex == selectedIndex
            
            val distanceFromCenter = if (isSpacer) 999 else abs(optIndex - selectedIndex)
            val alpha = when {
                isSpacer -> 0f
                isSelected -> 1f
                distanceFromCenter == 1 -> 0.5f
                else -> 0.25f
            }
            val fontSize = if (isSelected) 22.sp else 16.sp

            Box(
                modifier = Modifier
                    .height(ITEM_HEIGHT_DP.dp)
                    .fillMaxWidth()
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                if (!isSpacer) {
                    Text(
                        text = options[optIndex],
                        fontSize = fontSize,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = DarkText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
