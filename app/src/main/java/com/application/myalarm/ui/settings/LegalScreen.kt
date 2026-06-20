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
            text = "Last updated: June 20, 2026",
            fontSize = 12.sp,
            color = SubtitleGray
        )
        HorizontalDivider(color = Color(0xFFF0F0F0))

        LegalSection(
            title = "1. Acceptance of Terms",
            body = "By downloading, installing, or using this application (the \"Service\"), you agree to be bound by these Terms of Service. If you do not agree to these terms, you must not install or use the Service."
        )

        LegalSection(
            title = "2. Disclaimer of Warranties",
            body = "THE SERVICE IS PROVIDED ON AN \"AS IS\" AND \"AS AVAILABLE\" BASIS. THE DEVELOPER DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, REGARDING THE RELIABILITY, TIMELINESS, OR ACCURACY OF THE ALARMS.\n\nBECAUSE MOBILE OPERATING SYSTEMS MAY RESTRICT BACKGROUND TASKS, ENFORCE AGGRESSIVE BATTERY OPTIMIZATIONS, OR ENCOUNTER SYSTEM INTERRUPTIONS, WE CANNOT GUARANTEE THAT ALARMS WILL TRIGGER UNDER ALL CIRCUMSTANCES. IT IS YOUR RESPONSIBILITY TO ENSURE YOUR DEVICE PERMISSIONS (SUCH AS AUTOSTART, NOTIFICATIONS, AND LOCK SCREEN OVERLAYS) ARE PROPERLY CONFIGURED."
        )

        LegalSection(
            title = "3. Limitation of Liability",
            body = "IN NO EVENT SHALL THE DEVELOPER OR CONTRIBUTORS BE LIABLE FOR ANY INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES, INCLUDING BUT NOT LIMITED TO LOST TIME, APPOINTMENTS, INCOME, TRAVEL ARRANGEMENTS, OR OTHER OBLIGATIONS, ARISING OUT OF OR IN CONNECTION WITH THE FAILURE OF THE APP TO SOUND OR DISPLAY AN ALARM, SYSTEM OVERWRITES, OR CUSTOM USER SETTINGS."
        )

        LegalSection(
            title = "4. User Conduct & Missions",
            body = "The Service incorporates interactive wake-up missions (Math, Shaking, Camera Scanning, etc.) designed to prevent oversleeping. Users agree to utilize these features as designed. Circumventing or disabling these missions is at the user's own risk."
        )

        LegalSection(
            title = "5. Intellectual Property",
            body = "This app is an independent development. All custom logos, graphics, and code created for this application belong to the developer. All references to other third-party libraries (CameraX, ML Kit, Room) are governed by their respective open-source licenses."
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
            text = "Last updated: June 20, 2026",
            fontSize = 12.sp,
            color = SubtitleGray
        )
        HorizontalDivider(color = Color(0xFFF0F0F0))

        LegalSection(
            title = "1. Information We Process Locally",
            body = "Our application is designed with user privacy as our highest priority. All features and permissions process data locally on your device. We do not transmit or share any personal data with remote servers.\n\n" +
                    "• Camera Data: The camera permission is used solely to verify active missions (scanning QR codes/barcodes or taking specific object photos). Real-time image processing is executed entirely on-device using local machine learning APIs. No images or videos are saved, stored, or sent over the network.\n\n" +
                    "• Motion & Sensor Data: Accelerometer and step count sensors are processed locally on the device to count steps or detect shaking to turn off active alarms. This data is processed dynamically and is not saved.\n\n" +
                    "• Database Records: Alarm schedules, labels, settings, and mission success histories are saved securely in your device's private sandbox storage (Room DB and DataStore). This data is completely deleted when the app is uninstalled."
        )

        LegalSection(
            title = "2. Network Permissions",
            body = "Our app requests network access solely to perform background queries checking for software updates from our official updates repository. We do not gather analytics, track locations, or deploy user telemetry identifiers."
        )

        LegalSection(
            title = "3. Third-Party Libraries",
            body = "The app utilizes standard Android dependencies, including Google ML Kit and CameraX APIs. These tools run locally and conform to standard Android privacy declarations."
        )

        LegalSection(
            title = "4. Changes to This Policy",
            body = "We may update this Privacy Policy from time to time. Any updates will be displayed inside the application settings panel."
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
