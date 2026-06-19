package com.application.myalarm.ui.alarms

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val OrangeLight = Color(0xFFFFF3E0)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)

private data class SoundItem(
    val name: String,
    val description: String,
    val isFree: Boolean
)

private val soundsList = listOf(
    SoundItem("Basic", "Friendly default alarm tone (Kalimba)", true),
    SoundItem("Alarm Clock", "Classic loud alarm clock ringing", true),
    SoundItem("Bugle Tune", "Triumphant military bugle horn call", true),
    SoundItem("Medium Bell", "Resonant metallic bell ringing near", true),
    SoundItem("Short Beeps", "Quick alarm notification beeps", true),
    SoundItem("Clown Horn", "Funny circus comedy honk sound", true),
    SoundItem("Wake Up", "Energizing modern morning melody", false),
    SoundItem("Nature", "Calming acoustic synth melody", false),
    SoundItem("Digital Watch", "Beeping of a digital wristwatch", false),
    SoundItem("Spaceship", "Sci-fi ship alarm theme", false),
    SoundItem("Dosimeter", "Clicking radiation dosimeter alarm", false),
    SoundItem("Phone Alerts", "Assorted electronic phone rings", false),
    SoundItem("Computer Sounds", "Retro computer alert notifications", false),
    SoundItem("Alien Beam", "Deep pulsing extraterrestrial beam", false),
    SoundItem("Electric Ring", "High voltage electric ringtone sound", false),
    SoundItem("High Low Sweep", "Pitch-sweeping alert sound effect", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundPickerScreen(
    viewModel: AlarmEditViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val freeSounds = remember { soundsList.filter { it.isFree } }
    val premiumSounds = remember { soundsList.filter { !it.isFree } }

    var playingSoundName by remember { mutableStateOf<String?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            viewModel.customSoundUri = uri.toString()
            viewModel.soundName = "Custom Song"
        }
    }

    val playSound = { name: String ->
        if (playingSoundName == name) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            playingSoundName = null
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            try {
                val mp = if (name == "Custom Song") {
                    val uriStr = viewModel.customSoundUri
                    if (!uriStr.isNullOrEmpty()) {
                        MediaPlayer.create(context, Uri.parse(uriStr))
                    } else null
                } else {
                    val packageName = context.packageName
                    val resName = when (name) {
                        "Basic" -> "basic_alarm"
                        "Alarm Clock" -> "alarm_clock"
                        "Bugle Tune" -> "bugle_tune"
                        "Medium Bell" -> "medium_bell_ringing_near"
                        "Short Beeps" -> "beep_short"
                        "Clown Horn" -> "clown_horn"
                        "Wake Up" -> "wake_up_alarm"
                        "Nature" -> "nature_alarm"
                        "Digital Watch" -> "digital_watch_alarm"
                        "Spaceship" -> "spaceship_alarm"
                        "Dosimeter" -> "dosimeter_alarm"
                        "Phone Alerts" -> "phone_alerts_and_rings"
                        "Computer Sounds" -> "assorted_computer_sounds"
                        "Alien Beam" -> "alien_beam"
                        "Electric Ring" -> "electric_ring_long"
                        "High Low Sweep" -> "high_to_low_sweep"
                        else -> "basic_alarm"
                    }
                    val resId = context.resources.getIdentifier(resName, "raw", packageName)
                    if (resId != 0) {
                        MediaPlayer.create(context, resId)
                    } else {
                        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        if (uri != null) MediaPlayer.create(context, uri) else null
                    }
                }

                mp?.let {
                    it.isLooping = true
                    val vol = viewModel.soundVolume
                    it.setVolume(vol, vol)
                    it.start()
                    mediaPlayer = it
                    playingSoundName = name
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = Localizer.t("Alarm sound"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        onBack()
                    }) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(text = Localizer.t("VOLUME"))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeDown, contentDescription = Localizer.t("Volume Down"), tint = OrangePrimary)
                        Slider(
                            value = viewModel.soundVolume,
                            onValueChange = {
                                viewModel.soundVolume = it
                                mediaPlayer?.setVolume(it, it)
                            },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = OrangePrimary,
                                activeTrackColor = OrangePrimary,
                                inactiveTrackColor = OrangeLight
                            ),
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                        )
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = Localizer.t("Volume Up"), tint = OrangePrimary)
                    }
                }
            }

            item {
                SectionHeader(text = Localizer.t("CUSTOM SONG"))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (viewModel.soundName == "Custom Song") OrangeLight else CardWhite
                    ),
                    border = if (viewModel.soundName == "Custom Song") BorderStroke(1.5.dp, OrangePrimary) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(OrangePrimary)
                                    .clickable {
                                        if (!viewModel.customSoundUri.isNullOrEmpty()) {
                                            playSound("Custom Song")
                                        } else {
                                            filePickerLauncher.launch(arrayOf("audio/*"))
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (playingSoundName == "Custom Song") Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = Localizer.t("Preview"),
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        filePickerLauncher.launch(arrayOf("audio/*"))
                                    }
                            ) {
                                Text(
                                    text = if (!viewModel.customSoundUri.isNullOrEmpty()) {
                                        val resolvedUri = Uri.parse(viewModel.customSoundUri)
                                        resolvedUri.lastPathSegment ?: Localizer.t("Custom Song")
                                    } else Localizer.t("Select Custom Song"),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                                Text(
                                    text = Localizer.t("Choose any audio file from device"),
                                    fontSize = 12.sp,
                                    color = SubtitleGray
                                )
                            }

                            if (viewModel.soundName == "Custom Song") {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = OrangePrimary,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            viewModel.soundName = "Custom Song"
                                            onBack()
                                        }
                                )
                            }
                        }
                    }
                }
            }

            item {
                SectionHeader(text = Localizer.t("FREE"))
            }

            items(freeSounds) { sound ->
                SoundCard(
                    sound = sound,
                    isSelected = viewModel.soundName == sound.name,
                    isPlaying = playingSoundName == sound.name,
                    onPlayClick = { playSound(sound.name) },
                    onClick = {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        viewModel.soundName = sound.name
                        onBack()
                    }
                )
            }

            item {
                SectionHeader(text = Localizer.t("PREMIUM"))
            }

            items(premiumSounds) { sound ->
                SoundCard(
                    sound = sound,
                    isSelected = viewModel.soundName == sound.name,
                    isPlaying = playingSoundName == sound.name,
                    onPlayClick = { playSound(sound.name) },
                    onClick = {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        viewModel.soundName = sound.name
                        onBack()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = Localizer.t("Tap ▶ to preview."),
                    fontSize = 13.sp,
                    color = SubtitleGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SoundCard(
    sound: SoundItem,
    isSelected: Boolean,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OrangeLight else CardWhite
        ),
        border = if (isSelected) BorderStroke(1.5.dp, OrangePrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(OrangePrimary)
                    .clickable { onPlayClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) Localizer.t("Stop Preview") else Localizer.t("Play Preview"),
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Localizer.t(sound.name),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = Localizer.t(sound.description),
                    fontSize = 12.sp,
                    color = SubtitleGray
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
