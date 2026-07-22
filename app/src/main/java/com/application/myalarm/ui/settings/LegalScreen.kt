package com.application.myalarm.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.myalarm.util.Localizer

private val OrangePrimary = Color(0xFFFF8C00)
private val LightBackground = Color(0xFFF5F5F5)
private val CardWhite = Color(0xFFFFFFFF)
private val DarkText = Color(0xFF2D2D2D)
private val SubtitleGray = Color(0xFF9E9E9E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(
    type: String, // "terms" or "privacy"
    onBack: () -> Unit
) {
    val isTerms = type == "terms"
    val title = if (isTerms) Localizer.t("Terms of Service") else Localizer.t("Privacy Policy")

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        text = title,
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
                            contentDescription = Localizer.t("Back"),
                            tint = DarkText
                        )
                    }
                },
                // Add empty box to balance title centering
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (isTerms) {
                        TermsOfServiceContent()
                    } else {
                        PrivacyPolicyContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsOfServiceContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Terms of Service",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = "Last updated: July 5, 2026",
            fontSize = 12.sp,
            color = SubtitleGray
        )
        HorizontalDivider(color = Color(0xFFF0F0F0))

        LegalSection(
            title = "1. Welcome & Agreement",
            body = "By downloading, installing, or using MyAlarm, you agree to these Terms of Service. If you do not agree, please uninstall and do not use the app."
        )

        LegalSection(
            title = "2. Disclaimer: We Do Our Best, But...",
            body = "Our goal is to wake you up on time! However, because Android devices enforce aggressive battery saving, autostart blocks, and background limits, we cannot guarantee that alarms will sound in 100% of situations.\n\n" +
                    "It is your responsibility to configure your device correctly. Please check that MyAlarm has permissions for Autostart, Notifications, and Displaying Over Other Apps, and ensure it is excluded from battery optimizations."
        )

        LegalSection(
            title = "3. Limitation of Liability",
            body = "We want you to succeed, but we cannot be held responsible for any consequences of a missed alarm (such as being late to work, missing appointments, missing flights, or any lost income)."
        )

        LegalSection(
            title = "4. Wake-Up Missions",
            body = "Our wake-up missions (Math, Shaking, Typing, etc.) are built to help you get out of bed. Attempting to bypass, hack, or disable these missions is at your own risk."
        )

        LegalSection(
            title = "5. Ownership",
            body = "All app code, designs, and logos belong to us. Any third-party open-source libraries used in the app belong to their respective developers and are governed by their own licenses."
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun PrivacyPolicyContent() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Privacy Policy",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = "Last updated: July 5, 2026",
            fontSize = 12.sp,
            color = SubtitleGray
        )
        HorizontalDivider(color = Color(0xFFF0F0F0))

        LegalSection(
            title = "1. 100% Local Processing (Your Data Stays on Your Device)",
            body = "Your privacy is our top priority. We do not gather, store, or transmit your personal data to remote servers. Everything happens locally on your phone:\n\n" +
                    "• Camera: The camera permission is only used to scan barcodes/QR codes or take pictures for active missions. Image verification runs completely on-device. We never save, store, or share your photos or videos.\n\n" +
                    "• Sensors: Accelerometer and step-counter data are processed in real-time to detect steps or shaking to turn off alarms. This data is never saved or shared.\n\n" +
                    "• Settings & History: Alarm schedules, preferences, and completion histories are stored securely in your device's private sandbox database (Room DB). They are completely deleted if you uninstall the app."
        )

        LegalSection(
            title = "2. Network and Internet Use",
            body = "We only use the network connection to check for software updates from our official repository. We do not show ads, run analytics, track your location, or collect telemetry data."
        )

        LegalSection(
            title = "3. Third-Party Services",
            body = "We use Android CameraX and Google ML Kit to process camera images for missions. These tools execute entirely on your device and follow standard Android privacy rules."
        )

        LegalSection(
            title = "4. Policy Updates",
            body = "We may update this policy occasionally. Any updates will be displayed inside this settings panel."
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LegalSection(title: String, body: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            fontSize = 13.sp,
            color = DarkText,
            lineHeight = 18.sp
        )
    }
}
