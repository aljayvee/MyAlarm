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
import androidx.compose.ui.unit.dp

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

    var updateInfo by remember { mutableStateOf<com.application.myalarm.update.AppUpdateInfo?>(null) }

    LaunchedEffect(onboardingCompleted) {
        if (onboardingCompleted == true) {
            com.application.myalarm.update.AppUpdateChecker.checkForUpdate(context) { info ->
                updateInfo = info
            }
        }
    }

    updateInfo?.let { info ->
        AppUpdateDialog(
            updateInfo = info,
            onDismiss = {
                if (!info.forceUpdate) {
                    updateInfo = null
                }
            }
        )
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

    var currentRoute by remember { mutableStateOf("home") }
    val backStack = remember { mutableStateListOf("home") }
    
    // Lift the editViewModel so that selected mission/sound changes are retained
    val editViewModel: AlarmEditViewModel = viewModel()

    val navigate = { route: String ->
        backStack.add(route)
        currentRoute = route
    }

    val navigateBack = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            currentRoute = backStack.last()
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "alarms", "insights", "settings")) {
                NavigationBar(containerColor = Color.White) {
                    val tabs = listOf(
                        Triple("home", "Home", Icons.Default.Home),
                        Triple("alarms", "Alarms", Icons.Default.Alarm),
                        Triple("insights", "Insights", Icons.Default.Assessment),
                        Triple("settings", "Settings", Icons.Default.Settings)
                    )
                    
                    tabs.forEach { (route, label, icon) ->
                        val isSelected = currentRoute == route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != route) {
                                    backStack.clear()
                                    backStack.add(route)
                                    currentRoute = route
                                }
                            },
                            label = { 
                                Text(
                                    text = com.application.myalarm.util.Localizer.t(label), 
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                ) 
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = OrangePrimary,
                                selectedTextColor = OrangePrimary,
                                unselectedIconColor = SubtitleGray,
                                unselectedTextColor = SubtitleGray,
                                indicatorColor = OrangeLight
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                currentRoute == "home" -> {
                    HomeScreen(onNavigate = navigate)
                }
                currentRoute == "alarms" -> {
                    AlarmsScreen(onNavigate = navigate)
                }
                currentRoute == "insights" -> {
                    InsightsScreen()
                }
                currentRoute == "settings" -> {
                    SettingsScreen()
                }
                currentRoute.startsWith("alarm_edit/") -> {
                    val alarmId = currentRoute.substringAfter("alarm_edit/").toLongOrNull() ?: -1L
                    AlarmEditScreen(
                        alarmId = alarmId,
                        viewModel = editViewModel,
                        onNavigate = navigate,
                        onBack = navigateBack
                    )
                }
                currentRoute == "mission_picker" -> {
                    MissionPickerScreen(
                        viewModel = editViewModel,
                        onBack = navigateBack
                    )
                }
                currentRoute == "sound_picker" -> {
                    SoundPickerScreen(
                        viewModel = editViewModel,
                        onBack = navigateBack
                    )
                }
            }
        }
    }
}

@Composable
fun AppUpdateDialog(
    updateInfo: com.application.myalarm.update.AppUpdateInfo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableStateOf(0) }
    var downloadError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = {
            if (!updateInfo.forceUpdate && !isDownloading) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = "New Update Available",
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        text = {
            Column {
                Text(
                    text = "A new version (${updateInfo.latestVersionName}) is available. Please update to get the latest features and fixes.",
                    color = DarkText,
                    fontSize = 14.sp
                )
                if (!updateInfo.releaseNotes.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "What's New:",
                        fontWeight = FontWeight.SemiBold,
                        color = OrangePrimary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = updateInfo.releaseNotes,
                        color = SubtitleGray,
                        fontSize = 12.sp
                    )
                }
                if (isDownloading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Downloading... $downloadProgress%",
                        fontWeight = FontWeight.SemiBold,
                        color = OrangePrimary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { downloadProgress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = OrangePrimary,
                        trackColor = OrangeLight
                    )
                }
                if (downloadError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = downloadError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isDownloading = true
                    downloadError = null
                    com.application.myalarm.update.AppUpdateChecker.downloadApk(
                        context = context,
                        url = updateInfo.apkUrl,
                        onProgress = { progress ->
                            downloadProgress = progress
                        },
                        onComplete = { file ->
                            isDownloading = false
                            if (file != null && file.exists()) {
                                try {
                                    val apkUri = androidx.core.content.FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(apkUri, "application/vnd.android.package-archive")
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    downloadError = "Installation failed: ${e.message}"
                                }
                            } else {
                                downloadError = "Download failed. Please try again."
                            }
                        }
                    )
                },
                enabled = !isDownloading,
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Text(if (isDownloading) "Downloading..." else "Update", color = Color.White)
            }
        },
        dismissButton = {
            if (!updateInfo.forceUpdate && !isDownloading) {
                TextButton(onClick = onDismiss) {
                    Text("Back", color = SubtitleGray)
                }
            }
        },
        containerColor = Color.White,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
    )
}

