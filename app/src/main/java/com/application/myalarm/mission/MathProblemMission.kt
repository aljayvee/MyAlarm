package com.application.myalarm.mission

import com.application.myalarm.util.Localizer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AccentOrange = Color(0xFFFF8C00)
private val LightOrange = Color(0xFFFFA726)

private data class MathQuestion(
    val expression: String,
    val answer: Int
)

private fun generateQuestion(): MathQuestion {
    val operation = listOf("+", "-", "×").random()
    return when (operation) {
        "+" -> {
            val a = (10..99).random()
            val b = (10..99).random()
            MathQuestion("$a + $b", a + b)
        }
        "-" -> {
            val a = (10..99).random()
            val b = (10..a).random()
            MathQuestion("$a − $b", a - b)
        }
        "×" -> {
            val a = (2..12).random()
            val b = (2..12).random()
            MathQuestion("$a × $b", a * b)
        }
        else -> MathQuestion("1 + 1", 2)
    }
}

@Composable
fun MathProblemMission(onMissionComplete: () -> Unit) {
    val totalProblems = 3
    var currentProblem by remember { mutableIntStateOf(0) }
    var questions by remember {
        mutableStateOf(List(totalProblems) { generateQuestion() })
    }
    var userAnswer by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val question = questions[currentProblem]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.Calculate,
                contentDescription = Localizer.t("Math"),
                modifier = Modifier.size(48.dp),
                tint = AccentOrange
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = Localizer.t("Math Problem"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Localizer.t("Problem {number} of {total}")
                    .replace("{number}", (currentProblem + 1).toString())
                    .replace("{total}", totalProblems.toString()),
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { currentProblem.toFloat() / totalProblems.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = AccentOrange,
                trackColor = Color(0xFFE0E0E0),
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.expression,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "= ?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = userAnswer,
                onValueChange = { newValue ->
                    if (newValue.all { c -> c.isDigit() || c == '-' }) {
                        userAnswer = newValue
                        showError = false
                    }
                },
                label = { Text(Localizer.t("Your answer")) },
                placeholder = { Text(Localizer.t("Enter number")) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val parsed = userAnswer.toIntOrNull()
                        if (parsed == null) {
                            showError = true
                            errorMessage = Localizer.t("Please enter a valid number")
                        } else if (parsed != question.answer) {
                            showError = true
                            errorMessage = Localizer.t("Wrong answer, try again!")
                            userAnswer = ""
                        } else {
                            showError = false
                            userAnswer = ""
                            if (currentProblem < totalProblems - 1) {
                                currentProblem++
                            } else {
                                onMissionComplete()
                            }
                        }
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    cursorColor = AccentOrange,
                    focusedLabelColor = AccentOrange
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = showError,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val parsed = userAnswer.toIntOrNull()
                    if (parsed == null) {
                        showError = true
                        errorMessage = Localizer.t("Please enter a valid number")
                    } else if (parsed != question.answer) {
                        showError = true
                        errorMessage = Localizer.t("Wrong answer, try again!")
                        userAnswer = ""
                    } else {
                        showError = false
                        userAnswer = ""
                        if (currentProblem < totalProblems - 1) {
                            currentProblem++
                        } else {
                            onMissionComplete()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localizer.t("Submit"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(totalProblems) { index ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = when {
                                    index < currentProblem -> Color(0xFF4CAF50)
                                    index == currentProblem -> AccentOrange
                                    else -> Color(0xFFE0E0E0)
                                },
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                    if (index < totalProblems - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}
