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
private val SubtitleGray = Color(0xFF757575)

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
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
fun TermsOfServiceContent(
    bodyTextSizeSp: Int = 13,
    titleTextSizeSp: Int = 15
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(
                text = Localizer.t("Terms of Service"),
                fontSize = (titleTextSizeSp + 7).sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = Localizer.t("Last updated: July 23, 2026"),
                fontSize = 12.sp,
                color = SubtitleGray
            )
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        LegalSection(
            title = Localizer.t("1. Introduction and Acceptance of Terms"),
            body = Localizer.t("Welcome to Riser! These Terms of Service explain the rules and guidelines for using our mobile application. By downloading, installing, or using Riser, you agree to be bound by these Terms.\n\nIf you do not agree with any part of these rules, please do not use the application and uninstall it from your device."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("2. Purpose of the Application"),
            body = Localizer.t("Riser is a smart alarm clock application designed to help you wake up reliably and build healthy morning habits. The app provides alarm scheduling, snooze controls, wake-up analytics, and interactive wake-up challenges called Missions, such as Math problems, Step Counting, Shaking, Barcode or QR code scanning, Memory puzzles, and Typing tests."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("3. App Permissions and Phone Settings"),
            body = Localizer.t("To ensure your alarms ring precisely when scheduled, Riser requires certain system permissions on your phone:\n\n• Exact Alarm Permission: Allows your phone to wake up and trigger the alarm at the exact minute you set.\n\n• Notification and Full Screen Alert Permissions: Allows the alarm screen and sound to display over your lock screen when an alarm goes off.\n\n• Battery Saver and OEM App Restrictions: Many phone manufacturers, including Xiaomi, TECNO, Samsung, and Huawei, include aggressive battery-saving features that automatically close background apps. For your alarms to ring reliably, you must exclude Riser from battery optimization and enable any required Autostart or Background Activity settings in your phone settings."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("4. Wake-Up Challenges and Mission Integrity"),
            body = Localizer.t("Our wake-up challenges are built to stimulate your mind and body so you do not accidentally fall back asleep. While an alarm is ringing, you must complete your selected challenge to dismiss the sound.\n\nAttempting to force-close the app, turn off your phone, or uninstall Riser during an active alarm to bypass a mission is done at your own discretion."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("5. Limitation of Liability and General Disclaimer"),
            body = Localizer.t("We work hard to make Riser as dependable as possible. However, mobile devices can occasionally experience unexpected software or hardware issues, such as a drained battery, a phone system crash, silent sound modes, or restrictive system app killers.\n\nTherefore, Riser is provided on an AS IS and AS AVAILABLE basis. We cannot guarantee that an alarm will sound in 100% of all circumstances.\n\nTo the maximum extent permitted by law, the developer of Riser shall not be held legally or financially responsible for any missed alarms or any resulting consequences, including being late for work, missing school, missing appointments, missing flights, or any lost income."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("6. App Ownership and Intellectual Property"),
            body = Localizer.t("All code, visual designs, logos, graphics, and custom features inside Riser belong exclusively to the app developer and are protected by copyright laws. You may not copy, reverse engineer, or redistribute any part of the application.\n\nAny third-party open-source libraries used in the app, such as Google ML Kit and Android CameraX, belong to their respective owners and are used under their open-source licenses."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("7. Updates to These Terms"),
            body = Localizer.t("We may update these Terms from time to time to reflect changes in Android system rules or new app features. When updates occur, we will revise the Last updated date at the top of this screen. Continued use of Riser after an update signifies your acceptance of the updated Terms."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("8. Contact Us"),
            body = Localizer.t("If you have any questions, suggestions, or concerns regarding these Terms of Service, please contact us through the Settings tab inside the app."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun PrivacyPolicyContent(
    bodyTextSizeSp: Int = 13,
    titleTextSizeSp: Int = 15
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column {
            Text(
                text = Localizer.t("Privacy Policy"),
                fontSize = (titleTextSizeSp + 7).sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = Localizer.t("Last updated: July 23, 2026"),
                fontSize = 12.sp,
                color = SubtitleGray
            )
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        LegalSection(
            title = Localizer.t("1. Our Privacy Pledge: 100% Local On-Device Processing"),
            body = Localizer.t("Your privacy is our highest priority. Riser is built on a clear principle: Your personal data belongs to you, and it stays entirely on your device.\n\nWe do not collect, store, or upload your personal information to external cloud servers. We do not require you to create an account, sign in, or provide an email address. Everything operates locally on your phone."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("2. Device Permissions Breakdown"),
            body = Localizer.t("To deliver alarm features and wake-up challenges, Riser requests specific device permissions. Here is exactly what each permission does and why your privacy is safe:\n\n• Camera Access for Barcode, QR, and Photo Missions: Used only during an active wake-up challenge to scan barcodes or verify photos. Camera images pass through temporary phone memory and are processed in real time using on-device artificial intelligence powered by Google ML Kit. We never save, store, record, or upload your photos or camera feeds.\n\n• Motion and Accelerometer Sensors for Step and Shake Missions: Used only during an active alarm challenge to detect physical movement, count steps, or measure phone shakes. Sensor data is processed instantly in real time and is erased immediately after the challenge ends.\n\n• Internal App Storage for Local Database: Used to save your alarm times, sound choices, snooze stats, and app options locally on your phone. This information is stored in a private, encrypted Android sandbox that no other app can access."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("3. Advertising, Tracking, and Network Usage"),
            body = Localizer.t("We believe in a clean, non-intrusive user experience:\n\n• No Advertisements: Riser does not display third-party banner ads or video advertisements.\n\n• No Analytics or Telemetry: We do not use third-party analytics tools, behavioral profiling scripts, or advertising identifiers.\n\n• No Location Tracking: We do not track, collect, or request your GPS physical location.\n\n• Internet Connection: Internet access is used exclusively to check for official app software updates from our verified repository."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("4. Data Retention and How to Delete Your Data"),
            body = Localizer.t("Because all data is stored locally on your device, you remain in complete control at all times:\n\n• Clearing App Storage: You can clear all saved alarm schedules, settings, and history anytime in your phone Settings under Apps, Riser, Storage, Clear Data.\n\n• Uninstalling the App: Uninstalling Riser automatically and permanently deletes 100% of the data stored by the app from your phone."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("5. Your Global Legal Privacy Rights"),
            body = Localizer.t("Riser complies with standard international data protection regulations, including the European Union General Data Protection Regulation and California Consumer Privacy Act:\n\n• Right to Access and Know: You can view all stored stats and history directly inside the app Insights tab.\n\n• Right to Erasure: You can wipe all app data at any time by clearing app data or uninstalling.\n\n• Right to Opt-Out and Non-Discrimination: Because we collect zero personal data and sell zero data to third parties, there is no data to sell or share."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("6. Protection of Children Privacy"),
            body = Localizer.t("Riser is safe for users of all ages. Because the app operates 100% locally on your phone and gathers no personal information, it fully complies with the Children's Online Privacy Protection Act and General Data Protection Regulation child protection guidelines."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        LegalSection(
            title = Localizer.t("7. Policy Updates and Contact Information"),
            body = Localizer.t("We may update this Privacy Policy occasionally to reflect software updates or legal regulations. Revisions will be posted directly inside this screen. If you have questions about our privacy standards, please contact us through the Settings tab inside the app."),
            bodyTextSizeSp = bodyTextSizeSp,
            titleTextSizeSp = titleTextSizeSp
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LegalSection(
    title: String,
    body: String,
    bodyTextSizeSp: Int = 13,
    titleTextSizeSp: Int = 15
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = title,
            fontSize = titleTextSizeSp.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = body,
            fontSize = bodyTextSizeSp.sp,
            color = Color(0xFF333333),
            lineHeight = (bodyTextSizeSp + 7).sp
        )
    }
}
