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

    // Permissions State
    val isOemDevice = remember { OemSettingsHelper.isOemDevice() }

    var notificationPermissionGranted by remember { mutableStateOf(checkNotificationPermission(context)) }
    var overlayPermissionGranted by remember { mutableStateOf(Settings.canDrawOverlays(context)) }
    var lockScreenPermissionGranted by remember { mutableStateOf(!isOemDevice) }
    var cameraPermissionGranted by remember { mutableStateOf(checkCameraPermission(context)) }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        notificationPermissionGranted = isGranted
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted = isGranted
    }

    // Lifecycle observer to re-check permissions when returning from Settings screen
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationPermissionGranted = checkNotificationPermission(context)
                overlayPermissionGranted = Settings.canDrawOverlays(context)
                cameraPermissionGranted = checkCameraPermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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

    val isNextEnabled = remember(currentStep, struggleReason, preferredChallenge, notificationPermissionGranted, overlayPermissionGranted, lockScreenPermissionGranted, cameraPermissionGranted) {
        when (currentStep) {
            3 -> struggleReason.isNotEmpty() && preferredChallenge.isNotEmpty()
            4 -> notificationPermissionGranted && overlayPermissionGranted && cameraPermissionGranted && lockScreenPermissionGranted
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
                                4 -> StepPermissions(
                                    notificationGranted = notificationPermissionGranted,
                                    overlayGranted = overlayPermissionGranted,
                                    lockScreenGranted = lockScreenPermissionGranted,
                                    cameraGranted = cameraPermissionGranted,
                                    isOemDevice = isOemDevice,
                                    onRequestNotification = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            notificationPermissionGranted = true
                                        }
                                    },
                                    onRequestOverlay = {
                                        val intent = Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:${context.packageName}")
                                        )
                                        context.startActivity(intent)
                                    },
                                    onRequestLockScreen = {
                                        try {
                                            val intent = OemSettingsHelper.getOemSettingsIntent(context)
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                                data = Uri.fromParts("package", context.packageName, null)
                                            }
                                            context.startActivity(intent)
                                        }
                                        lockScreenPermissionGranted = true
                                    },
                                    onRequestCamera = {
                                        cameraLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                )
                                5 -> StepLanguage(
                                    selectedLangCode = selectedLangCode,
                                    onLanguageSelect = { code ->
                                        coroutineScope.launch {
                                            app.userPreferences.updateSelectedLanguage(code)
                                        }
                                    }
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
                if (currentStep > 1) {
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
                } else {
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
                            text = Localizer.t("Started"),
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
            text = Localizer.t("Why SnoozeOff?"),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = Localizer.t("Are you tired of oversleeping and hitting snooze repeatedly? SnoozeOff is designed to wake you up completely. To silence the alarm, you must complete interactive missions (solving math problems, shaking the phone, taking photos of the sky, or walking a specified step count). Once the alarm triggers, navigation controls are restricted to ensure you don't cheat!"),
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
            text = Localizer.t("Hi! I am the creator of this alarm application. I developed SnoozeOff to help you break bad sleeping habits, cultivate morning self-discipline, and get out of bed on time with engaging wake-up missions. Thank you for choosing my app to master your mornings!"),
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = Localizer.t("Help Us Know You"),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Text(
            text = Localizer.t("Tell us about your waking habits so we can assist you better."),
            fontSize = 12.sp,
            color = SubtitleGray
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Question 1
        Text(
            text = Localizer.t("1. What is your biggest morning challenge?"),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        val reasons = listOf("Oversleeping through normal alarms", "Clicking snooze repeatedly", "Waking up feeling tired")
        reasons.forEach { reason ->
            val isSelected = struggleReason == reason
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
                    .clickable { onStruggleReasonSelect(reason) }
                    .padding(12.dp)
            ) {
                Text(
                    text = Localizer.t(reason),
                    fontSize = 12.sp,
                    color = if (isSelected) OrangePrimary else DarkText,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
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

        val challenges = listOf("Mental warm-up (Math, Memory)", "Physical activity (Shaking, Push-ups)", "Out-of-bed actions (Sky Photo, Code scan)")
        challenges.forEach { challenge ->
            val isSelected = preferredChallenge == challenge
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
                    .clickable { onPreferredChallengeSelect(challenge) }
                    .padding(12.dp)
            ) {
                Text(
                    text = Localizer.t(challenge),
                    fontSize = 12.sp,
                    color = if (isSelected) OrangePrimary else DarkText,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
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
                        text = if (notificationGranted) Localizer.t("Access Allowed") else Localizer.t("Required to trigger wake-up notifications"),
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
                        text = Localizer.t("Draw Over Other Apps"),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = if (overlayGranted) Localizer.t("Access Allowed") else Localizer.t("Required to display overlays over other apps"),
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
                            text = Localizer.t("Show on Lock Screen"),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = if (lockScreenGranted) Localizer.t("Access Allowed") else Localizer.t("Required to display overlays on the lock screen"),
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
                        text = if (cameraGranted) Localizer.t("Access Allowed") else Localizer.t("Required for photo capture and scanning missions"),
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
