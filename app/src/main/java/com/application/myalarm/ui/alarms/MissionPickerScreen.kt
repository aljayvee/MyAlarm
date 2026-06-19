package com.application.myalarm.ui.alarms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.application.myalarm.mission.*
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionPickerScreen(
    viewModel: AlarmEditViewModel,
    onBack: () -> Unit
) {
    val allMissions = remember { MissionType.values().toList() }
    val freeMissions = remember { allMissions.filter { it.isFree } }
    val premiumMissions = remember { allMissions.filter { !it.isFree } }

    var demoMission by remember { mutableStateOf<MissionType?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = {
                        Text(
                            text = Localizer.t("Choose mission"),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(text = Localizer.t("FREE"))
                }

                items(freeMissions) { mission ->
                    MissionCard(
                        mission = mission,
                        isSelected = viewModel.missionType == mission.name,
                        onClick = {
                            viewModel.missionType = mission.name
                            onBack()
                        },
                        onDemoClick = {
                            demoMission = mission
                        }
                    )
                }

                item {
                    SectionHeader(text = Localizer.t("PREMIUM"))
                }

                items(premiumMissions) { mission ->
                    MissionCard(
                        mission = mission,
                        isSelected = viewModel.missionType == mission.name,
                        onClick = {
                            viewModel.missionType = mission.name
                            onBack()
                        },
                        onDemoClick = {
                            demoMission = mission
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (demoMission != null) {
            MissionDemoOverlay(
                missionType = demoMission!!,
                onClose = { demoMission = null }
            )
        }
    }
}

@Composable
private fun MissionCard(
    mission: MissionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDemoClick: () -> Unit
) {
    val icon = remember(mission) {
        when (mission) {
            MissionType.SHAKE -> Icons.Default.Vibration
            MissionType.SKY_PHOTO -> Icons.Default.WbSunny
            MissionType.MAKE_YOUR_BED -> Icons.Default.Bed
            MissionType.OBJECT_HUNT -> Icons.Default.Search
            MissionType.QUOTE_OF_THE_DAY -> Icons.Default.FormatQuote
            MissionType.AFFIRMATION -> Icons.Default.Favorite
            MissionType.PUSH_UPS -> Icons.Default.FitnessCenter
            MissionType.MEMORY -> Icons.Default.GridView
            MissionType.TYPING -> Icons.Default.Keyboard
            MissionType.QR_CODE -> Icons.Default.QrCode
            MissionType.BARCODE -> Icons.Default.QrCodeScanner
            MissionType.STEP_COUNT -> Icons.Default.DirectionsRun
            else -> Icons.Default.Calculate
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OrangeLight else CardWhite
        ),
        border = if (isSelected) BorderStroke(1.5.dp, OrangePrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Selection Click Target
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (isSelected) Color.White else OrangeLight,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = Localizer.t(mission.displayName),
                        tint = OrangePrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = Localizer.t(mission.displayName),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Localizer.t(mission.description),
                        fontSize = 12.sp,
                        color = SubtitleGray,
                        lineHeight = 16.sp
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = OrangePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Demo click target
            Box(
                modifier = Modifier
                    .clickable(onClick = onDemoClick)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Demo",
                    tint = SubtitleGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = SubtitleGray,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
private fun MissionDemoOverlay(
    missionType: MissionType,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Demo Mode Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2A2A2A))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Localizer.t("Demonstration Mode"),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = Localizer.t("Testing: {name}").replace("{name}", Localizer.t(missionType.displayName)),
                            fontSize = 12.sp,
                            color = Color(0xFFBDBDBD)
                        )
                    }

                    TextButton(
                        onClick = onClose,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFFA726))
                    ) {
                        Text(Localizer.t("Exit Demo"), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Render mission with isTesting = true
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (missionType) {
                    MissionType.MATH_PROBLEM -> MathProblemMission(onMissionComplete = onClose)
                    MissionType.SHAKE -> ShakeMission(onMissionComplete = onClose, isTesting = true)
                    MissionType.SKY_PHOTO -> SkyPhotoMission(onMissionComplete = onClose)
                    MissionType.MAKE_YOUR_BED -> MakeYourBedMission(onMissionComplete = onClose)
                    MissionType.OBJECT_HUNT -> ObjectHuntMission(onMissionComplete = onClose)
                    MissionType.QUOTE_OF_THE_DAY -> QuoteOfTheDayMission(onMissionComplete = onClose)
                    MissionType.AFFIRMATION -> AffirmationMission(onMissionComplete = onClose)
                    MissionType.PUSH_UPS -> PushUpsMission(onMissionComplete = onClose, isTesting = true)
                    MissionType.MEMORY -> MemoryMission(onMissionComplete = onClose)
                    MissionType.TYPING -> TypingMission(onMissionComplete = onClose)
                    MissionType.QR_CODE -> QRCodeMission(targetValue = "DEMO_QR_CODE", onMissionComplete = onClose, isTesting = true)
                    MissionType.BARCODE -> BarcodeMission(targetValue = "DEMO_BARCODE", onMissionComplete = onClose, isTesting = true)
                    MissionType.STEP_COUNT -> StepCountMission(level = 2, onMissionComplete = onClose, isTesting = true)
                }
            }
        }
    }
}
