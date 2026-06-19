package com.application.myalarm.mission

import com.application.myalarm.util.Localizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentOrange = Color(0xFFFF8C00)
private val LightOrange = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)

private val affirmations = listOf(
    "I am grateful for this new day and its opportunities",
    "I choose to be happy, healthy, and positive today",
    "I am capable of achieving all my goals today",
    "I believe in myself and my abilities completely",
    "I am strong, resilient, and ready for anything"
)

@Composable
fun AffirmationMission(onMissionComplete: () -> Unit) {
    val baseTarget = remember { affirmations.random() }
    val target = remember(baseTarget, Localizer.currentLanguage) { Localizer.t(baseTarget) }
    var typedText by remember { mutableStateOf("") }

    val annotatedText = remember(typedText) {
        buildAnnotatedString {
            for (i in target.indices) {
                if (i < typedText.length) {
                    val isMatch = target[i].equals(typedText[i], ignoreCase = true)
                    withStyle(
                        style = SpanStyle(
                            color = if (isMatch) Color(0xFF4CAF50) else Color(0xFFE53935),
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(target[i])
                    }
                } else {
                    withStyle(style = SpanStyle(color = SubtitleGray)) {
                        append(target[i])
                    }
                }
            }
        }
    }

    val isComplete = remember(typedText) {
        typedText.trim().equals(target.trim(), ignoreCase = true)
    }

    LaunchedEffect(isComplete) {
        if (isComplete) {
            onMissionComplete()
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = Localizer.t("Affirmation"),
                tint = AccentOrange,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = Localizer.t("Type the affirmation exactly:"),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SubtitleGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = LightOrange),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = annotatedText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = typedText,
                onValueChange = {
                    if (it.length <= target.length) {
                        typedText = it
                    }
                },
                placeholder = { Text(Localizer.t("Start typing here...")) },
                singleLine = false,
                maxLines = 3,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isComplete) {
                            onMissionComplete()
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = AccentOrange
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
