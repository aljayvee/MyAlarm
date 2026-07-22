package com.application.myalarm.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.myalarm.ui.alarms.AlarmEditScreen
import com.application.myalarm.ui.alarms.AlarmEditViewModel
import com.application.myalarm.ui.alarms.AlarmsScreen
import com.application.myalarm.ui.alarms.MissionPickerScreen
import com.application.myalarm.ui.alarms.SoundPickerScreen
import com.application.myalarm.ui.home.HomeScreen
import com.application.myalarm.ui.insights.InsightsScreen
import com.application.myalarm.ui.settings.SettingsScreen
import com.application.myalarm.ui.settings.LegalScreen
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import com.application.myalarm.AlarmApplication
import com.application.myalarm.ui.onboarding.OnboardingScreen
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.saveable.rememberSaveable
import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeLight = Color(0xFFFFF3E0)
private val SubtitleGray = Color(0xFF9E9E9E)
private val DarkText = Color(0xFF2D2D2D)

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val app = context.applicationContext as AlarmApplication
    val userPrefs = app.userPreferences
    val onboardingCompleted by userPrefs.onboardingCompleted.collectAsState(initial = null)
    val selectedLanguage by userPrefs.selectedLanguage.collectAsState(initial = "en")

    val lastVersionCode by userPrefs.lastVersionCode.collectAsState(initial = 0)
    var showUpdateSuccessDialog by remember { mutableStateOf(false) }
    var updatedVersionName by remember { mutableStateOf("") }

    var showNotificationPermissionDialog by remember { mutableStateOf(false) }
    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    fun checkNotificationPermission(ctx: android.content.Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    LaunchedEffect(onboardingCompleted) {
        if (onboardingCompleted == true) {
            if (!checkNotificationPermission(context)) {
                showNotificationPermissionDialog = true
            }
        }
    }

    if (showNotificationPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationPermissionDialog = false },
            title = {
                Text(
                    text = Localizer.t("Enable Notifications"),
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            },
            text = {
                Text(
                    text = Localizer.t("To ensure you never miss your alarms, MyAlarm needs permission to send you notifications. This allows us to ring alarms on time, show active timer controls, and alert you when a mission is pending."),
                    color = DarkText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotificationPermissionDialog = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = OrangePrimary)
                ) {
                    Text(Localizer.t("Enable"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showNotificationPermissionDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                ) {
                    Text(Localizer.t("Not Now"))
                }
            },
            containerColor = Color.White,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
        )
    }

    LaunchedEffect(lastVersionCode, onboardingCompleted) {
        if (onboardingCompleted == true) {
            val currentCode = com.application.myalarm.BuildConfig.VERSION_CODE
            if (lastVersionCode > 0 && currentCode > lastVersionCode) {
                updatedVersionName = com.application.myalarm.BuildConfig.VERSION_NAME
                showUpdateSuccessDialog = true
            }
            if (currentCode != lastVersionCode) {
                userPrefs.updateLastVersionCode(currentCode)
            }
        }
    }

    if (showUpdateSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateSuccessDialog = false },
            title = {
                Text(
                    text = "Update Successful",
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            },
            text = {
                Text(
                    text = "The app has been successfully updated to version $updatedVersionName!",
                    color = DarkText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showUpdateSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
        )
    }

    LaunchedEffect(onboardingCompleted) {
        if (onboardingCompleted == true) {
            com.application.myalarm.update.AppUpdateChecker.checkForUpdate(context) { info ->
                com.application.myalarm.update.AppUpdateChecker.updateAvailableInBackground.value = (info != null)
            }
        }
    }

    LaunchedEffect(selectedLanguage) {
        val matchedLang = com.application.myalarm.util.Localizer.Language.values().find { it.code == selectedLanguage } ?: com.application.myalarm.util.Localizer.Language.ENGLISH
        com.application.myalarm.util.Localizer.currentLanguage = matchedLang
    }

    if (onboardingCompleted == null) {
        // Simple loading screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF8F0)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = OrangePrimary)
        }
        return
    }

    if (onboardingCompleted == false) {
        OnboardingScreen(onComplete = {})
        return
    }

    val navState = rememberSaveable(saver = AppNavigationState.Saver) {
        AppNavigationState()
    }
    
    // Intercept phone/device back button press when backstack has other elements
    BackHandler(enabled = navState.backStack.size > 1) {
        navState.navigateBack()
    }

    // Lift the editViewModel so that selected mission/sound changes are retained
    val editViewModel: AlarmEditViewModel = viewModel()

    Scaffold(
        containerColor = Color(0xFFF5F5F5), // Set soft grey background color matching screens
        bottomBar = {
            if (navState.currentRoute in listOf("home", "alarms", "insights", "settings")) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp), // spacing from screen bottom
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(280.dp) // Apple-style compact dock width
                            .height(60.dp), // Sleeker height without text labels
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp), // Perfect pill shape (height / 2)
                        color = Color(0xEEFAFAFA), // Soft white with transparency
                        tonalElevation = 0.dp, // Disable tonal tint
                        shadowElevation = 8.dp, // Soft shadow
                        border = BorderStroke(
                            width = 1.dp,
                            color = Color(0x18000000) // very light subtle border outline
                        )
                    ) {
                        @OptIn(ExperimentalMaterial3Api::class)
                        CompositionLocalProvider(LocalRippleConfiguration provides null) {
                            NavigationBar(
                                containerColor = Color.Transparent, // Surface draws background
                                modifier = Modifier.fillMaxSize(),
                                windowInsets = WindowInsets(0, 0, 0, 0) // clear default insets
                            ) {
                                val tabs = listOf(
                                    Triple("home", "Home", Icons.Default.Home),
                                    Triple("alarms", "Alarms", Icons.Default.Alarm),
                                    Triple("insights", "Insights", Icons.Default.Assessment),
                                    Triple("settings", "Settings", Icons.Default.Settings)
                                )
                                
                                tabs.forEach { (route, label, icon) ->
                                    val isSelected = navState.currentRoute == route
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            if (navState.currentRoute != route) {
                                                navState.selectTab(route)
                                            }
                                        },
                                        icon = { 
                                            Icon(
                                                imageVector = icon, 
                                                contentDescription = label,
                                                modifier = Modifier.size(24.dp)
                                            ) 
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangePrimary,
                                            unselectedIconColor = SubtitleGray,
                                            indicatorColor = Color.Transparent // iOS style: no background pill indicator!
                                        ),
                                        alwaysShowLabel = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(androidx.compose.ui.unit.LayoutDirection.Ltr)
                )
        ) {
            when {
                navState.currentRoute == "home" -> {
                    HomeScreen(onNavigate = navState::navigate)
                }
                navState.currentRoute == "alarms" -> {
                    AlarmsScreen(onNavigate = navState::navigate)
                }
                navState.currentRoute == "insights" -> {
                    InsightsScreen()
                }
                navState.currentRoute == "settings" -> {
                    SettingsScreen(onNavigate = navState::navigate)
                }
                navState.currentRoute == "terms_of_service" -> {
                    LegalScreen(type = "terms", onBack = navState::navigateBack)
                }
                navState.currentRoute == "privacy_policy" -> {
                    LegalScreen(type = "privacy", onBack = navState::navigateBack)
                }
                navState.currentRoute == "update" -> {
                    com.application.myalarm.ui.update.UpdateScreen(
                        onBack = navState::navigateBack
                    )
                }
                navState.currentRoute.startsWith("alarm_edit/") -> {
                    val alarmId = navState.currentRoute.substringAfter("alarm_edit/").toLongOrNull() ?: -1L
                    AlarmEditScreen(
                        alarmId = alarmId,
                        viewModel = editViewModel,
                        onNavigate = navState::navigate,
                        onBack = navState::navigateBack
                    )
                }
                navState.currentRoute == "mission_picker" -> {
                    MissionPickerScreen(
                        viewModel = editViewModel,
                        onBack = navState::navigateBack
                    )
                }
                navState.currentRoute == "sound_picker" -> {
                    SoundPickerScreen(
                        viewModel = editViewModel,
                        onBack = navState::navigateBack
                    )
                }
            }
        }
    }
}



