package com.application.myalarm.mission

import com.application.myalarm.util.Localizer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDate

private val AccentOrange = Color(0xFFFF8C00)
private val LightOrange = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)

private val quotes = listOf(
    Pair("The secret of getting ahead is getting started.", "Mark Twain"),
    Pair("Believe you can and you're halfway there.", "Theodore Roosevelt"),
    Pair("The only way to do great work is to love what you do.", "Steve Jobs"),
    Pair("The best way to predict the future is to create it.", "Abraham Lincoln"),
    Pair("Don't count the days, make the days count.", "Muhammad Ali")
)

@Composable
fun QuoteOfTheDayMission(onMissionComplete: () -> Unit) {
    val dayOfYear = remember { LocalDate.now().dayOfYear }
    val quoteIndex = remember { dayOfYear % quotes.size }
    val (quoteText, quoteAuthor) = remember { quotes[quoteIndex] }

    var secondsLeft by remember { mutableIntStateOf(5) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
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
                imageVector = Icons.Default.FormatQuote,
                contentDescription = Localizer.t("Quote"),
                tint = AccentOrange,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = LightOrange),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\"${Localizer.t(quoteText)}\"",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        color = DarkText,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "— $quoteAuthor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SubtitleGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onMissionComplete,
                enabled = secondsLeft == 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkText,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFE0E0E0),
                    disabledContentColor = SubtitleGray
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (secondsLeft > 0) {
                    Text(text = Localizer.t("Read for {seconds}s...").replace("{seconds}", secondsLeft.toString()), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text(text = Localizer.t("I've read this"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
