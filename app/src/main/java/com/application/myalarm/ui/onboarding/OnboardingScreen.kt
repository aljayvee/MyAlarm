package com.application.myalarm.ui.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.application.myalarm.AlarmApplication
import com.application.myalarm.mission.MissionType
import com.application.myalarm.ui.settings.PrivacyPolicyContent
import com.application.myalarm.ui.settings.TermsOfServiceContent
import com.application.myalarm.util.Localizer
import com.application.myalarm.util.OemSettingsHelper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.launch
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeAccent = Color(0xFFFFA726)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val CardWhite = Color(0xFFFFFFFF)
private val SuccessGreen = Color(0xFF4CAF50)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as AlarmApplication
    val coroutineScope = rememberCoroutineScope()

    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 6
    var legalAgreed by remember { mutableStateOf(false) }

    // Language state
    var selectedLangCode by remember { mutableStateOf("en") }
    LaunchedEffect(Unit) {
        app.userPreferences.selectedLanguage.collect { code ->
            selectedLangCode = code
            val matchedLang = Localizer.Language.values().find { it.code == code } ?: Localizer.Language.ENGLISH
            Localizer.currentLanguage = matchedLang
        }
    }

    // Questionnaire State
    var struggleReason by remember { mutableStateOf("") }
    var preferredChallenge by remember { mutableStateOf("") }

    val isNextEnabled = remember(currentStep, struggleReason, preferredChallenge, legalAgreed) {
        when (currentStep) {
            3 -> struggleReason.isNotEmpty() && preferredChallenge.isNotEmpty()
            5 -> legalAgreed
            else -> true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF8F0), Color(0xFFFFF3E0))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Indicator dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 1..totalSteps) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (currentStep == i) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (currentStep == i) OrangePrimary else SubtitleGray.copy(alpha = 0.4f))
                    )
                }
            }

            // Main Content Area inside a Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    @OptIn(ExperimentalAnimationApi::class)
                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                // Slide and fade (morphing) transition
                                (slideInHorizontally(animationSpec = tween(700)) { it } + 
                                 fadeIn(animationSpec = tween(700)) + 
                                 scaleIn(initialScale = 0.92f, animationSpec = tween(700)))
                                .togetherWith(
                                 slideOutHorizontally(animationSpec = tween(700)) { -it } + 
                                 fadeOut(animationSpec = tween(700)) + 
                                 scaleOut(targetScale = 0.92f, animationSpec = tween(700))
                                )
                            } else {
                                (slideInHorizontally(animationSpec = tween(700)) { -it } + 
                                 fadeIn(animationSpec = tween(700)) + 
                                 scaleIn(initialScale = 0.92f, animationSpec = tween(700)))
                                .togetherWith(
                                 slideOutHorizontally(animationSpec = tween(700)) { it } + 
                                 fadeOut(animationSpec = tween(700)) + 
                                 scaleOut(targetScale = 0.92f, animationSpec = tween(700))
                                )
                            }
                        },
                        label = "OnboardingStepTransition"
                    ) { step ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (step) {
                                1 -> StepWhyUseApp()
                                2 -> StepDeveloperIntro()
                                3 -> StepQuestionnaire(
                                    struggleReason = struggleReason,
                                    onStruggleReasonSelect = { struggleReason = it },
                                    preferredChallenge = preferredChallenge,
                                    onPreferredChallengeSelect = { preferredChallenge = it }
                                )
                                4 -> StepLanguage(
                                    selectedLangCode = selectedLangCode,
                                    onLanguageSelect = { code ->
                                        coroutineScope.launch {
                                            app.userPreferences.updateSelectedLanguage(code)
                                        }
                                    }
                                )
                                5 -> StepTermsAndPrivacy(
                                    agreed = legalAgreed,
                                    onAgreedChecked = { legalAgreed = it }
                                )
                                6 -> StepGetStarted()
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStep > 1 && currentStep < totalSteps) {
                    OutlinedButton(
                        onClick = { currentStep -= 1 },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangePrimary),
                        border = borderStroke(OrangePrimary)
                    ) {
                        Text(
                            text = Localizer.t("Back"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                } else if (currentStep < totalSteps) {
                    Spacer(modifier = Modifier.width(10.dp)) // Placeholder
                }

                if (currentStep < totalSteps) {
                    Button(
                        onClick = { currentStep += 1 },
                        enabled = isNextEnabled,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary,
                            disabledContainerColor = OrangePrimary.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = Localizer.t("Next"),
                                fontWeight = FontWeight.Bold,
                                color = if (isNextEnabled) Color.White else Color.White.copy(alpha = 0.6f),
                                fontSize = 15.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = if (isNextEnabled) Color.White else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                app.userPreferences.setOnboardingCompleted(true)
                                onComplete()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = Localizer.t("Complete Setup & Enter App"),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun borderStroke(color: Color) = androidx.compose.foundation.BorderStroke(1.dp, color)

@Composable
private fun StepWhyUseApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(OrangeLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = null,
                tint = OrangePrimary,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = Localizer.t("Why Riser?"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = Localizer.t("Are you tired of oversleeping and hitting snooze repeatedly? Riser is designed to wake you up completely. To silence the alarm, you must complete interactive missions (solving math problems, shaking the phone, taking photos of the sky, or walking a specified step count). Once the alarm triggers, navigation controls are restricted to ensure you don't cheat!"),
            fontSize = 13.sp,
            color = SubtitleGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepDeveloperIntro() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE3F2FD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color(0xFF1E88E5),
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = Localizer.t("Meet the Developer"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Aljayvee Versola",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = OrangePrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = Localizer.t("Hi! I am the creator of this alarm application. I developed Riser to help you break bad sleeping habits, cultivate morning self-discipline, and get out of bed on time with engaging wake-up missions. Thank you for choosing my app to master your mornings!"),
            fontSize = 13.sp,
            color = SubtitleGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepQuestionnaire(
    struggleReason: String,
    onStruggleReasonSelect: (String) -> Unit,
    preferredChallenge: String,
    onPreferredChallengeSelect: (String) -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as AlarmApplication
    val coroutineScope = rememberCoroutineScope()

    val onChallengeChosen: (String) -> Unit = { challenge ->
        onPreferredChallengeSelect(challenge)
        val missionType = when {
            challenge.contains("Mental", ignoreCase = true) -> MissionType.MATH_PROBLEM.name
            challenge.contains("Physical", ignoreCase = true) -> MissionType.SHAKE.name
            challenge.contains("Out-of-bed", ignoreCase = true) -> MissionType.SKY_PHOTO.name
            else -> MissionType.MATH_PROBLEM.name
        }
        coroutineScope.launch {
            app.userPreferences.updateDefaultMissionType(missionType)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = Localizer.t("Help Us Know You"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Text(
            text = Localizer.t("Tell us about your waking habits so we can assist you better and configure your default alarm settings."),
            fontSize = 12.sp,
            color = SubtitleGray,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Question 1
        Text(
            text = Localizer.t("1. What is your biggest morning challenge?"),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        val reasons = listOf(
            Triple("Oversleeping through normal alarms", Icons.Default.Alarm, "Hard Sleeper"),
            Triple("Clicking snooze repeatedly", Icons.Default.Snooze, "Snooze Habit"),
            Triple("Waking up feeling tired", Icons.Default.Bedtime, "Morning Fatigue")
        )

        reasons.forEach { (reason, icon, badge) ->
            val isSelected = struggleReason == reason
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStruggleReasonSelect(reason) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) OrangeLight else Color(0xFFF7F7F7)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 0.dp,
                    color = if (isSelected) OrangePrimary else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) OrangePrimary else SubtitleGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localizer.t(reason),
                            fontSize = 13.sp,
                            color = if (isSelected) DarkText else Color(0xFF424242),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = OrangePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Question 2
        Text(
            text = Localizer.t("2. What type of wake-up mission do you prefer?"),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        val challenges = listOf(
            Triple("Mental warm-up (Math, Memory)", Icons.Default.Psychology, "Math & Logic"),
            Triple("Physical activity (Shaking, Push-ups)", Icons.Default.FitnessCenter, "Motion & Movement"),
            Triple("Out-of-bed actions (Sky Photo, Code scan)", Icons.Default.CameraAlt, "Photo & Scan")
        )

        challenges.forEach { (challenge, icon, badge) ->
            val isSelected = preferredChallenge == challenge
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChallengeChosen(challenge) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) OrangeLight else Color(0xFFF7F7F7)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 1.5.dp else 0.dp,
                    color = if (isSelected) OrangePrimary else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) OrangePrimary else SubtitleGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localizer.t(challenge),
                            fontSize = 13.sp,
                            color = if (isSelected) DarkText else Color(0xFF424242),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Text(
                            text = "Default Mission: $badge",
                            fontSize = 11.sp,
                            color = if (isSelected) OrangePrimary else SubtitleGray
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = OrangePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepPermissions(
    notificationGranted: Boolean,
    overlayGranted: Boolean,
    lockScreenGranted: Boolean,
    cameraGranted: Boolean,
    isOemDevice: Boolean,
    onRequestNotification: () -> Unit,
    onRequestOverlay: () -> Unit,
    onRequestLockScreen: () -> Unit,
    onRequestCamera: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localizer.t("Grant Access Permissions"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Text(
            text = Localizer.t("For alarms to ring reliably and show overlay screens, these permissions are required."),
            fontSize = 12.sp,
            color = SubtitleGray
        )

        Text(
            text = Localizer.t("Without these permissions, the alarm may not ring reliably, or you might easily bypass the wake-up missions."),
            fontSize = 11.sp,
            color = OrangePrimary,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Notification Permission Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (notificationGranted) SuccessGreen else OrangePrimary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Localizer.t("Notifications"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = if (notificationGranted) Localizer.t("Access Allowed") else Localizer.t("Required to trigger wake-up notifications. Without this, the app cannot show background alarm alerts or status bar indicators."),
                        fontSize = 11.sp,
                        color = SubtitleGray
                    )
                }

                if (notificationGranted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Granted",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Button(
                        onClick = onRequestNotification,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(Localizer.t("Grant"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Overlay Permission Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = null,
                    tint = if (overlayGranted) SuccessGreen else OrangePrimary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Localizer.t("Alarms & Reminders"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = if (overlayGranted) Localizer.t("Access Allowed") else Localizer.t("Required to schedule exact alarm triggers on time when your device enters sleep or Doze mode."),
                        fontSize = 11.sp,
                        color = SubtitleGray
                    )
                }

                if (overlayGranted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Granted",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Button(
                        onClick = onRequestOverlay,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(Localizer.t("Grant"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Show on Lock Screen Card (OEM only)
        if (isOemDevice) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (lockScreenGranted) SuccessGreen else OrangePrimary,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localizer.t("Wake Screen on Lock Screen"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = if (lockScreenGranted) Localizer.t("Access Allowed") else Localizer.t("Allows alarms to wake up the screen and present the full-screen ringing UI over the lock screen."),
                            fontSize = 11.sp,
                            color = SubtitleGray
                        )
                    }

                    if (lockScreenGranted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Granted",
                            tint = SuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Button(
                            onClick = onRequestLockScreen,
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(Localizer.t("Grant"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Camera Permission Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = if (cameraGranted) SuccessGreen else OrangePrimary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Localizer.t("Camera Access"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = if (cameraGranted) Localizer.t("Access Allowed") else Localizer.t("Required for photo capture and scanning missions. This allows you to complete photo-based or barcode-scanning missions to silence the alarm."),
                        fontSize = 11.sp,
                        color = SubtitleGray
                    )
                }

                if (cameraGranted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Granted",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Button(
                        onClick = onRequestCamera,
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(Localizer.t("Grant"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepLanguage(
    selectedLangCode: String,
    onLanguageSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = Localizer.t("Select Language"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Text(
            text = Localizer.t("Choose your preferred language of use."),
            fontSize = 12.sp,
            color = SubtitleGray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Localizer.Language.values().forEach { lang ->
                val isSelected = selectedLangCode == lang.code
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) OrangeLight else Color(0xFFF5F5F5))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) OrangePrimary else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onLanguageSelect(lang.code) }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = lang.displayName,
                            fontSize = 13.sp,
                            color = if (isSelected) OrangePrimary else DarkText,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepGetStarted() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE8F5E9), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = Localizer.t("You're All Set!"),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = Localizer.t("Congratulations! Setup is complete and you are fully prepared to wake up disciplined. Go ahead, set your first alarm and conquer your mornings!"),
            fontSize = 13.sp,
            color = SubtitleGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun StepTermsAndPrivacy(
    agreed: Boolean,
    onAgreedChecked: (Boolean) -> Unit
) {
    var showPromptModal by remember { mutableStateOf(false) }
    var showReaderModal by remember { mutableStateOf(false) }
    var readerPage by remember { mutableIntStateOf(1) } // 1: Terms, 2: Privacy

    val scrollStateTerms = rememberScrollState()
    val scrollStatePrivacy = rememberScrollState()

    var isTermsScrolledToBottom by remember { mutableStateOf(false) }
    var isPrivacyScrolledToBottom by remember { mutableStateOf(false) }

    val atTermsBottom by remember {
        derivedStateOf {
            scrollStateTerms.maxValue > 0 && scrollStateTerms.value >= (scrollStateTerms.maxValue - 30)
        }
    }

    val atPrivacyBottom by remember {
        derivedStateOf {
            scrollStatePrivacy.maxValue > 0 && scrollStatePrivacy.value >= (scrollStatePrivacy.maxValue - 30)
        }
    }

    LaunchedEffect(atTermsBottom) {
        if (atTermsBottom) {
            isTermsScrolledToBottom = true
        }
    }

    LaunchedEffect(atPrivacyBottom) {
        if (atPrivacyBottom) {
            isPrivacyScrolledToBottom = true
        }
    }

    // Initial Prompt Modal ("Please read the agreement before proceeding")
    if (showPromptModal) {
        AlertDialog(
            onDismissRequest = { showPromptModal = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = OrangePrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = Localizer.t("Agreement Notice"),
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = Localizer.t("Please read the agreement before proceeding."),
                    color = DarkText,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPromptModal = false
                        readerPage = 1
                        showReaderModal = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = Localizer.t("Okay"),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showPromptModal = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkText)
                ) {
                    Text(
                        text = Localizer.t("Back"),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            containerColor = CardWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Full Interactive Reader Dialog with Mandatory Scroll Lock (Non-Bypassable)
    if (showReaderModal) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { /* Cannot bypass by clicking outside or back press */ },
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = CardWhite,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (readerPage == 1) Localizer.t("Terms of Service") else Localizer.t("Privacy Policy"),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = if (readerPage == 1) Localizer.t("Step 1 of 2 — Scroll to bottom to unlock Next") else Localizer.t("Step 2 of 2 — Scroll to bottom to unlock Done"),
                                fontSize = 11.sp,
                                color = OrangePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(onClick = { showReaderModal = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = SubtitleGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Scrollable Legal Content Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (readerPage == 1) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollStateTerms)
                                    .padding(bottom = 40.dp)
                            ) {
                                TermsOfServiceContent(
                                    bodyTextSizeSp = 15,
                                    titleTextSizeSp = 17
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if (isTermsScrolledToBottom) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFE8F5E9),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "✓ " + Localizer.t("Terms of Service read! You may now tap the button below."),
                                            fontSize = 12.sp,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            if (!isTermsScrolledToBottom) {
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = OrangePrimary,
                                    shadowElevation = 4.dp
                                ) {
                                    Text(
                                        text = "👇 " + Localizer.t("Please scroll to the very bottom of Terms of Service"),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollStatePrivacy)
                                    .padding(bottom = 40.dp)
                            ) {
                                PrivacyPolicyContent(
                                    bodyTextSizeSp = 15,
                                    titleTextSizeSp = 17
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if (isPrivacyScrolledToBottom) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFE8F5E9),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "✓ " + Localizer.t("Privacy Policy read! You may now tap Done below."),
                                            fontSize = 12.sp,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(10.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            if (!isPrivacyScrolledToBottom) {
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = OrangePrimary,
                                    shadowElevation = 4.dp
                                ) {
                                    Text(
                                        text = "👇 " + Localizer.t("Please scroll to the very bottom of Privacy Policy"),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Action Button (Locked until scrolled to bottom)
                    if (readerPage == 1) {
                        Button(
                            enabled = isTermsScrolledToBottom,
                            onClick = { readerPage = 2 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OrangePrimary,
                                disabledContainerColor = Color(0xFFE0E0E0)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = Localizer.t("I have read the Terms of Service"),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTermsScrolledToBottom) Color.White else SubtitleGray
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = if (isTermsScrolledToBottom) Color.White else SubtitleGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    } else {
                        Button(
                            enabled = isPrivacyScrolledToBottom,
                            onClick = {
                                showReaderModal = false
                                onAgreedChecked(true)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                disabledContainerColor = Color(0xFFE0E0E0)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isPrivacyScrolledToBottom) Color.White else SubtitleGray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = Localizer.t("Done"),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPrivacyScrolledToBottom) Color.White else SubtitleGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = Localizer.t("Terms & Privacy Agreement"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Text(
            text = Localizer.t("Please read and accept our Terms of Service and Privacy Policy to continue."),
            fontSize = 12.sp,
            color = SubtitleGray,
            lineHeight = 16.sp
        )

        // Read Agreement Card Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (agreed) {
                        onAgreedChecked(false)
                    } else {
                        showPromptModal = true
                    }
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (agreed) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (agreed) Color(0xFF4CAF50) else OrangePrimary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (agreed) Color(0xFFC8E6C9) else Color(0xFFFFE0B2),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (agreed) Icons.Default.VerifiedUser else Icons.Default.Description,
                        contentDescription = null,
                        tint = if (agreed) Color(0xFF2E7D32) else OrangePrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (agreed) Localizer.t("Agreements Met & Accepted") else Localizer.t("Read Terms & Privacy Policy"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (agreed) Localizer.t("You have read Terms of Service and Privacy Policy.") else Localizer.t("Tap here to read and accept agreements."),
                        fontSize = 12.sp,
                        color = SubtitleGray
                    )
                }

                Icon(
                    imageVector = if (agreed) Icons.Default.CheckCircle else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = if (agreed) Color(0xFF2E7D32) else OrangePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Checkbox Agreement Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (agreed) {
                        onAgreedChecked(false)
                    } else {
                        showPromptModal = true
                    }
                },
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (agreed) OrangeLight else Color(0xFFF5F5F5)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp)
            ) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            showPromptModal = true
                        } else {
                            onAgreedChecked(false)
                        }
                    },
                    colors = CheckboxDefaults.colors(checkedColor = OrangePrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localizer.t("I agree to the Terms of Service and Privacy Policy, and accept all consequences of missed alarms."),
                    fontSize = 12.sp,
                    color = DarkText,
                    lineHeight = 16.sp,
                    fontWeight = if (agreed) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

private fun checkNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}
