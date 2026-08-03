package com.application.myalarm.mission

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
import com.application.myalarm.util.Localizer

private val AccentOrange = Color(0xFFFF8C00)
private val LightOrange = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)

private enum class GameState {
    INTRO, SHOWING_PATTERN, USER_INPUT, CORRECT_FLASH, WRONG_FLASH
}

@Composable
fun MemoryMission(onMissionComplete: () -> Unit) {
    var currentRound by remember { mutableIntStateOf(1) }
    var gameState by remember { mutableStateOf(GameState.INTRO) }
    
    val patternLength = remember(currentRound) { 2 + currentRound } // Round 1: 3, Round 2: 4, Round 3: 5
    var targetPattern by remember { mutableStateOf<List<Int>>(emptyList()) }
    val userPattern = remember { mutableStateListOf<Int>() }

    var activeLitIndex by remember { mutableIntStateOf(-1) }
    var flashGreen by remember { mutableStateOf(false) }
    var flashRed by remember { mutableStateOf(false) }

    // Control game play
    LaunchedEffect(currentRound, gameState) {
        when (gameState) {
            GameState.INTRO -> {
                // Show "Round X" text for 1.5 seconds
                delay(1500L)
                // Generate pattern
                val newPattern = List(patternLength) { Random.nextInt(16) }
                targetPattern = newPattern
                userPattern.clear()
                gameState = GameState.SHOWING_PATTERN
            }
            GameState.SHOWING_PATTERN -> {
                delay(500L)
                for (index in targetPattern) {
                    activeLitIndex = index
                    delay(600L)
                    activeLitIndex = -1
                    delay(200L)
                }
                gameState = GameState.USER_INPUT
            }
            GameState.CORRECT_FLASH -> {
                flashGreen = true
                delay(600L)
                flashGreen = false
                if (currentRound < 3) {
                    currentRound++
                    gameState = GameState.INTRO
                } else {
                    onMissionComplete()
                }
            }
            GameState.WRONG_FLASH -> {
                flashRed = true
                delay(800L)
                flashRed = false
                // Restart current round
                gameState = GameState.INTRO
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("memory_target_pattern_${targetPattern.joinToString("_")}")
        ) {
            Text(
                text = Localizer.t("Memory Game"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Localizer.t("Round {number} of {total}")
                    .replace("{number}", currentRound.toString())
                    .replace("{total}", "3"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = AccentOrange,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Information/Status card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        flashGreen -> Color(0xFFE8F5E9)
                        flashRed -> Color(0xFFFFEBEE)
                        else -> LightOrange
                    }
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = when (gameState) {
                            GameState.INTRO -> Localizer.t("Prepare for Round {number}...").replace("{number}", currentRound.toString())
                            GameState.SHOWING_PATTERN -> Localizer.t("Watch the pattern closely!")
                            GameState.USER_INPUT -> Localizer.t("Repeat the sequence: {current} / {total}")
                                .replace("{current}", userPattern.size.toString())
                                .replace("{total}", targetPattern.size.toString())
                            GameState.CORRECT_FLASH -> Localizer.t("Correct! Get ready...")
                            GameState.WRONG_FLASH -> Localizer.t("Oops! Let's try again.")
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when {
                            flashGreen -> Color(0xFF2E7D32)
                            flashRed -> Color(0xFFC62828)
                            else -> DarkText
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4x4 Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0 until 4) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (col in 0 until 4) {
                            val cellIndex = row * 4 + col
                            val isLit = activeLitIndex == cellIndex
                            
                            val cellColor by animateColorAsState(
                                targetValue = when {
                                    flashGreen -> Color(0xFF4CAF50)
                                    flashRed -> Color(0xFFE53935)
                                    isLit -> AccentOrange
                                    else -> Color(0xFFEEEEEE)
                                },
                                label = "CellColor_$cellIndex"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(cellColor)
                                    .testTag("memory_cell_$cellIndex")
                                    .clickable(enabled = gameState == GameState.USER_INPUT) {
                                        val expectedIndex = targetPattern[userPattern.size]
                                        if (cellIndex == expectedIndex) {
                                            userPattern.add(cellIndex)
                                            if (userPattern.size == targetPattern.size) {
                                                gameState = GameState.CORRECT_FLASH
                                            }
                                        } else {
                                            gameState = GameState.WRONG_FLASH
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}
