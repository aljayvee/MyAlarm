package com.application.myalarm.ui.update

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.application.myalarm.update.AppUpdateChecker
import com.application.myalarm.util.Localizer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val OrangePrimary = Color(0xFFFF8C00)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val GreenSuccess = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    var showUpdateAvailableModal by remember { mutableStateOf(false) }
    var showInstallationSuccessModal by remember { mutableStateOf(false) }

    // Collect states from AppUpdateChecker
    val updateAvailable by remember { AppUpdateChecker.updateAvailableInBackground }
    val downloadState by remember { AppUpdateChecker.downloadState }
    val downloadProgress by remember { AppUpdateChecker.downloadProgress }
    val downloadSpeed by remember { AppUpdateChecker.downloadSpeedText }
    val fetchedInfo by remember { AppUpdateChecker.fetchedUpdateInfo }

    LaunchedEffect(Unit) {
        AppUpdateChecker.checkForUpdate(context) { info ->
            if (info != null && AppUpdateChecker.downloadState.value == AppUpdateChecker.DownloadState.IDLE) {
                showUpdateAvailableModal = true
            }
        }
    }

    // Modal dialog when updates are available
    if (showUpdateAvailableModal) {
        val verName = fetchedInfo?.latestVersionName ?: ""
        val verCode = fetchedInfo?.latestVersionCode ?: 0
        val notes = fetchedInfo?.releaseNotes ?: Localizer.t("Bug fixes and stability improvements.")

        AlertDialog(
            onDismissRequest = { showUpdateAvailableModal = false },
            title = {
                Text(
                    text = Localizer.t("New Update Available"),
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            },
            text = {
                Column {
                    Text(
                        text = "${Localizer.t("Version")}: $verName (${Localizer.t("Build")} $verCode)",
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${Localizer.t("Release Notes")}:\n$notes",
                        color = DarkText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = {
                            showUpdateAvailableModal = false
                            fetchedInfo?.apkUrl?.let { url ->
                                AppUpdateChecker.openInBrowser(context, url)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                    ) {
                        Text(Localizer.t("Browser"))
                    }
                    Button(
                        onClick = {
                            showUpdateAvailableModal = false
                            AppUpdateChecker.startRealDownload(context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text(Localizer.t("Download Now"))
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showUpdateAvailableModal = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = DarkText)
                ) {
                    Text(Localizer.t("Later"))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Modal dialog when installation completes
    if (showInstallationSuccessModal) {
        val verName = fetchedInfo?.latestVersionName ?: ""
        val verCode = fetchedInfo?.latestVersionCode ?: 0

        AlertDialog(
            onDismissRequest = { 
                showInstallationSuccessModal = false 
                AppUpdateChecker.updateAvailableInBackground.value = false
                AppUpdateChecker.resetSimulation()
                onBack()
            },
            title = {
                Text(
                    text = Localizer.t("Update Installed"),
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            },
            text = {
                Text(
                    text = "${Localizer.t("MyAlarm has been successfully updated to version")} $verName (${Localizer.t("Build")} $verCode)! ${Localizer.t("All new features are now active.")}",
                    color = DarkText,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showInstallationSuccessModal = false
                        AppUpdateChecker.updateAvailableInBackground.value = false
                        AppUpdateChecker.resetSimulation()
                        onBack()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = OrangePrimary)
                ) {
                    Text(Localizer.t("Great!"))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Localizer.t("Check for Updates"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = Localizer.t("Back"),
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightBackground
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                AppUpdateChecker.checkForUpdate(context) { info ->
                    isRefreshing = false
                    if (info != null) {
                        showUpdateAvailableModal = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                val currentVersion = com.application.myalarm.BuildConfig.VERSION_NAME
                val targetVersion = fetchedInfo?.latestVersionName ?: ""
                val targetCode = fetchedInfo?.latestVersionCode ?: 0

                // App info Card (Horizontal & Slim Height - Huawei AI Life style)
                // App info Card only appears if update is downloading or ready to install
                if (downloadState != AppUpdateChecker.DownloadState.IDLE) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Logo of the App (compact 64.dp)
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFFFB74D), OrangePrimary)
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = "MyAlarm Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Center: App details and download progress
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "MyAlarm",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "${Localizer.t("Version")} $currentVersion → $targetVersion (${Localizer.t("Build")} $targetCode)",
                                    fontSize = 12.sp,
                                    color = SubtitleGray
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                LinearProgressIndicator(
                                    progress = { downloadProgress.toFloat() / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = OrangePrimary,
                                    trackColor = Color(0xFFEEEEEE)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = when (downloadState) {
                                            AppUpdateChecker.DownloadState.DOWNLOADING -> {
                                                if (downloadSpeed.isNotEmpty()) {
                                                    "${Localizer.t("Downloading...")} • $downloadSpeed"
                                                } else {
                                                    Localizer.t("Downloading...")
                                                }
                                            }
                                            AppUpdateChecker.DownloadState.PAUSED -> Localizer.t("Paused")
                                            AppUpdateChecker.DownloadState.DOWNLOADED -> Localizer.t("Completed")
                                            else -> ""
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = SubtitleGray
                                    )
                                    Text(
                                        text = "$downloadProgress%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OrangePrimary
                                    )
                                }
                            }

                            // Right: Integrated compact Action Button
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            when (downloadState) {
                                AppUpdateChecker.DownloadState.DOWNLOADING -> {
                                    Button(
                                        onClick = {
                                            AppUpdateChecker.pauseSimulationDownload()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SubtitleGray),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                    ) {
                                        Text(
                                            text = Localizer.t("Pause"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                AppUpdateChecker.DownloadState.PAUSED -> {
                                    Button(
                                        onClick = {
                                            AppUpdateChecker.resumeSimulationDownload(scope)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                    ) {
                                        Text(
                                            text = Localizer.t("Resume"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                AppUpdateChecker.DownloadState.DOWNLOADED -> {
                                    Button(
                                        onClick = {
                                            val apkFile = java.io.File(java.io.File(context.getExternalFilesDir(null), "updates"), "update.apk")
                                            if (apkFile.exists()) {
                                                val verified = AppUpdateChecker.verifyAndInstallApk(context, apkFile)
                                                if (!verified) {
                                                    fetchedInfo?.apkUrl?.let { url ->
                                                        AppUpdateChecker.openInBrowser(context, url)
                                                    }
                                                }
                                            } else {
                                                fetchedInfo?.apkUrl?.let { url ->
                                                    AppUpdateChecker.openInBrowser(context, url)
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.height(28.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                                    ) {
                                        Text(
                                            text = Localizer.t("Install"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                } else if (updateAvailable) {
                    // Simple centered App logo & name presentation for IDLE state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFFFFB74D), OrangePrimary)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "MyAlarm Logo",
                                tint = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "MyAlarm",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${Localizer.t("Version")} $currentVersion → $targetVersion (${Localizer.t("Build")} $targetCode)",
                            fontSize = 13.sp,
                            color = SubtitleGray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Actions Section
                if (updateAvailable) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = Localizer.t("What's New:"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val releaseNotesText = fetchedInfo?.releaseNotes ?: Localizer.t("Bug fixes and performance improvements.")
                            Text(
                                text = releaseNotesText,
                                fontSize = 13.sp,
                                color = DarkText,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (downloadState == AppUpdateChecker.DownloadState.IDLE) {
                        Button(
                            onClick = {
                                AppUpdateChecker.startRealDownload(context)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = Localizer.t("Download Update"),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                fetchedInfo?.apkUrl?.let { url ->
                                    AppUpdateChecker.openInBrowser(context, url)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text(
                                text = Localizer.t("Download via Browser"),
                                fontSize = 14.sp,
                                color = DarkText
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Up to Date",
                        tint = GreenSuccess,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = Localizer.t("Your app is fully up to date!"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Localizer.t("Pull down to refresh and re-check for new versions."),
                        fontSize = 12.sp,
                        color = SubtitleGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

