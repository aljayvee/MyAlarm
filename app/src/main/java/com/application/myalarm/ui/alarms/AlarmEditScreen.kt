package com.application.myalarm.ui.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.myalarm.mission.MissionType
import com.application.myalarm.util.DateTimeUtils
import android.util.Size
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeAccent = Color(0xFFFFA726)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val GreenToggle = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmEditScreen(
    alarmId: Long,
    viewModel: AlarmEditViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    var isTimePickerVisible by remember { mutableStateOf(false) }
    var isCodeScannerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(alarmId) {
        viewModel.loadAlarm(alarmId)
    }

    val (timeString, amPm) = remember(viewModel.hour, viewModel.minute) {
        DateTimeUtils.formatTime12h(viewModel.hour, viewModel.minute)
    }

    val dayLetters = remember(com.application.myalarm.util.Localizer.currentLanguage) {
        listOf(
            com.application.myalarm.util.Localizer.t("Day_Mon_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Tue_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Wed_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Thu_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Fri_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Sat_Initial"),
            com.application.myalarm.util.Localizer.t("Day_Sun_Initial")
        )
    }

    val selectedMissionName = remember(viewModel.missionType, com.application.myalarm.util.Localizer.currentLanguage) {
        try {
            com.application.myalarm.util.Localizer.t(MissionType.valueOf(viewModel.missionType).displayName)
        } catch (e: Exception) {
            com.application.myalarm.util.Localizer.t("Math Problem")
        }
    }

    if (isTimePickerVisible) {
        TimePickerBottomSheet(
            initialHour = viewModel.hour,
            initialMinute = viewModel.minute,
            onDismiss = { isTimePickerVisible = false },
            onTimeSelected = { h, m ->
                viewModel.hour = h
                viewModel.minute = m
                isTimePickerVisible = false
            }
        )
    }

    if (isCodeScannerVisible) {
        CameraCodeScannerDialog(
            isQrCode = viewModel.missionType == "QR_CODE",
            onCodeScanned = { code ->
                viewModel.scannedCodeValue = code
                isCodeScannerVisible = false
            },
            onDismiss = { isCodeScannerVisible = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = if (viewModel.isEditing) com.application.myalarm.util.Localizer.t("Edit alarm") else com.application.myalarm.util.Localizer.t("New alarm"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = com.application.myalarm.util.Localizer.t("Back"),
                            tint = DarkText
                        )
                    }
                },
                actions = {
                    Text(
                        text = com.application.myalarm.util.Localizer.t("Save"),
                        color = OrangePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clickable {
                                viewModel.saveAlarm(onComplete = onBack)
                            }
                            .padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = LightBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Time Display Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = timeString,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = amPm,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubtitleGray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Button(
                        onClick = { isTimePickerVisible = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeLight,
                            contentColor = OrangePrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = com.application.myalarm.util.Localizer.t("Set Time"),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(com.application.myalarm.util.Localizer.t("Set Time"), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Label section
            SectionHeader(text = "LABEL")
            OutlinedTextField(
                value = viewModel.label,
                onValueChange = { viewModel.label = it },
                placeholder = { Text(com.application.myalarm.util.Localizer.t("Optional label"), color = SubtitleGray) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedBorderColor = OrangePrimary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = OrangePrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // Repeat section
            SectionHeader(text = "REPEAT")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    dayLetters.forEachIndexed { index, letter ->
                        val isSelected = viewModel.isDaySelected(index)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) OrangePrimary else Color(0xFFF0F0F0))
                                .clickable { viewModel.toggleDay(index) }
                        ) {
                            Text(
                                text = letter,
                                color = if (isSelected) Color.White else DarkText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Mission section
            SectionHeader(text = "MISSION")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .clickable { onNavigate("mission_picker") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(OrangeLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (viewModel.missionType) {
                                "SHAKE" -> Icons.Default.Vibration
                                "SKY_PHOTO" -> Icons.Default.WbSunny
                                "MAKE_YOUR_BED" -> Icons.Default.Bed
                                "OBJECT_HUNT" -> Icons.Default.Search
                                "QUOTE_OF_THE_DAY" -> Icons.Default.FormatQuote
                                "AFFIRMATION" -> Icons.Default.Favorite
                                "PUSH_UPS" -> Icons.Default.FitnessCenter
                                "MEMORY" -> Icons.Default.GridView
                                "TYPING" -> Icons.Default.Keyboard
                                "QR_CODE" -> Icons.Default.QrCode
                                "BARCODE" -> Icons.Default.QrCodeScanner
                                "STEP_COUNT" -> Icons.Default.DirectionsRun
                                else -> Icons.Default.Calculate
                            },
                            contentDescription = "Mission Icon",
                            tint = OrangePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedMissionName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = com.application.myalarm.util.Localizer.t("Tap to change"),
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = com.application.myalarm.util.Localizer.t("Tap to change"),
                        tint = SubtitleGray
                    )
                }
            }

            if (viewModel.missionType == "QR_CODE" || viewModel.missionType == "BARCODE") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (viewModel.missionType == "QR_CODE") com.application.myalarm.util.Localizer.t("QR Code Value") else com.application.myalarm.util.Localizer.t("Barcode Value"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SubtitleGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = viewModel.scannedCodeValue,
                                onValueChange = { viewModel.scannedCodeValue = it },
                                placeholder = { Text(com.application.myalarm.util.Localizer.t("Enter target code value"), color = SubtitleGray) },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = OrangePrimary,
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    cursorColor = OrangePrimary
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { isCodeScannerVisible = true },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(OrangePrimary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = com.application.myalarm.util.Localizer.t("Scan"),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            if (viewModel.missionType == "STEP_COUNT") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = com.application.myalarm.util.Localizer.t("Step Count Level"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = com.application.myalarm.util.Localizer.t("Level {number}").replace("{number}", viewModel.stepCountLevel.toString()),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangePrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = com.application.myalarm.util.Localizer.t("Level 1: 5 steps (Easy) to Level 10: 50 steps (Difficult)"),
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Slider(
                            value = viewModel.stepCountLevel.toFloat(),
                            onValueChange = { viewModel.stepCountLevel = it.toInt() },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = OrangePrimary,
                                activeTrackColor = OrangePrimary,
                                inactiveTrackColor = OrangeLight
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Sound section
            SectionHeader(text = "SOUND")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { onNavigate("sound_picker") },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(OrangeLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Sound Icon",
                            tint = OrangePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (viewModel.soundName == "Custom Song") {
                                if (!viewModel.customSoundUri.isNullOrEmpty()) {
                                    Uri.parse(viewModel.customSoundUri).lastPathSegment ?: com.application.myalarm.util.Localizer.t("Custom Song")
                                } else {
                                    com.application.myalarm.util.Localizer.t("Custom Song")
                                }
                            } else {
                                com.application.myalarm.util.Localizer.t(viewModel.soundName)
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = com.application.myalarm.util.Localizer.t("Tap to change sound"),
                            fontSize = 12.sp,
                            color = SubtitleGray
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = com.application.myalarm.util.Localizer.t("Tap to change sound"),
                        tint = SubtitleGray
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.VolumeDown, contentDescription = com.application.myalarm.util.Localizer.t("Volume Down"), tint = OrangePrimary)
                    Slider(
                        value = viewModel.soundVolume,
                        onValueChange = { viewModel.soundVolume = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = OrangePrimary,
                            activeTrackColor = OrangePrimary,
                            inactiveTrackColor = OrangeLight
                        ),
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = com.application.myalarm.util.Localizer.t("Volume Up"), tint = OrangePrimary)
                }
            }

            // Backup Alarm section
            SectionHeader(text = "BACKUP ALARM")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = com.application.myalarm.util.Localizer.t("Backup fire"),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }

                        Switch(
                            checked = viewModel.isBackupAlarmEnabled,
                            onCheckedChange = { viewModel.isBackupAlarmEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = GreenToggle,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE0E0E0),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }

                    if (viewModel.isBackupAlarmEnabled) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFECEFF1)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = com.application.myalarm.util.Localizer.t("Fire again after (min)"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkText
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = { viewModel.decrementBackupMinutes() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFFF0F0F0), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = com.application.myalarm.util.Localizer.t("Decrease"),
                                        tint = DarkText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = "${viewModel.backupAlarmMinutes}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )

                                IconButton(
                                    onClick = { viewModel.incrementBackupMinutes() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFFF0F0F0), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = com.application.myalarm.util.Localizer.t("Increase"),
                                        tint = DarkText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = com.application.myalarm.util.Localizer.t("If you don't dismiss within the chosen window, the alarm fires again automatically."),
                        fontSize = 12.sp,
                        color = SubtitleGray,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = com.application.myalarm.util.Localizer.t(text),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = SubtitleGray,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCodeScannerDialog(
    isQrCode: Boolean,
    onCodeScanned: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // CameraX elements
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }

    // ML Kit barcode scanner
    val scanner = remember { BarcodeScanning.getClient() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = { Text(if (isQrCode) com.application.myalarm.util.Localizer.t("Scan QR Code") else com.application.myalarm.util.Localizer.t("Scan Barcode")) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = com.application.myalarm.util.Localizer.t("Back"))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                // Camera Preview
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            imageAnalysis.setAnalyzer(executor) { imageProxy ->
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val image = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )
                                    scanner.process(image)
                                        .addOnSuccessListener { barcodes ->
                                            for (barcode in barcodes) {
                                                val rawValue = barcode.rawValue
                                                if (!rawValue.isNullOrEmpty()) {
                                                    onCodeScanned(rawValue)
                                                    break
                                                }
                                            }
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else {
                                    imageProxy.close()
                                }
                            }

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Simple overlay text
                Text(
                    text = com.application.myalarm.util.Localizer.t("Center the code in the screen"),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
