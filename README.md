# My Alarm (SnoozeOff Clone)

An advanced Jetpack Compose-based Android alarm application featuring interactive missions designed to prevent oversleeping and build consistent morning habits. This app is designed as a fully-featured clone of the "SnoozeOff" application, utilizing device sensors, local processing, and custom background persistence systems for robust reliability.

---

## 🌟 Key Features

*   **Interactive Dismissal Missions**:
    *   **Math Challenges**: Solve customizable arithmetic equations to test your cognitive alertness before dismissing the alarm.
    *   **Shake Mission**: Shake the device a predefined number of times (utilizing accelerometer sensors) to prove you are awake.
    *   **QR/Barcode Scanner**: Register a physical barcode or QR code (e.g., placed in your bathroom or kitchen) that you must scan to shut off the alarm.
    *   **Physical Tasks**: Challenges like push-ups or step counting tracked using local motion sensors.
*   **Intelligent Onboarding Flow**:
    *   Step-by-step onboarding walkthrough covering system permissions (Notifications, System Overlay, Camera, and Lock Screen options).
    *   User questionnaire regarding waking struggles and preferred challenges to tailor the initial experience.
    *   Multilingual language selector.
    *   Required Terms of Service and Privacy Policy checkbox detailing local processing rules and liability disclaimers.
*   **OEM Autostart & Power Management Integration**:
    *   Includes a specialized `OemSettingsHelper` to help users configure autostart, background execution, and lock screen settings for custom OEM skins (such as Infinix, TECNO, Xiaomi, Oppo, Vivo, etc.) to ensure reliable alarm triggering.
*   **Privacy-First & Fully Local**:
    *   All motion data, sensor inputs, and camera barcode scans are processed strictly local to the device; no personal details or scans are uploaded.
*   **Robust Background Scheduling**:
    *   Alarms persist through device reboots using `AlarmReceiver` and persistent `Room` DB storage.
    *   Alarm scheduling is wrapped in robust exception handling to prevent crashes on aggressive custom battery optimization systems (common in Android 13/14 OEM devices).

---

## 🛠️ Technology Stack

*   **UI Framework**: Jetpack Compose (Modern declarative UI)
*   **Programming Language**: Kotlin
*   **Database**: Room DB (SQLite) for persisting alarm configs and history logs
*   **Dependency Injection / Architecture**: MVVM architecture, Clean Code structure
*   **Android Jetpack Utilities**: Version Catalogs (libs.versions.toml), Coroutines, Flow

---

## 🌍 Localization Support

The app features comprehensive localizations handled through a custom localization system:
*   English
*   Chinese (Simplified, Traditional, Pinyin, Cantonese)
*   Arabic
*   Tagalog
*   Thai
*   Hindi

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio Jellyfish or newer.
*   Android SDK 26 (Android 8.0 Oreo) or higher.

### Build and Run
1. Clone the repository and open the project in Android Studio.
2. Build the project using Gradle or from the command line:
    ```bash
    ./gradlew assembleRelease
    ```
3. Locate the compiled production APK inside the release folder at:
   `app/release/alarm-prod.apk`

---

## 📋 Legal Rules & Disclaimers

Before starting the app, users must accept the following terms:
1.  **Local Processing**: All motion and camera scans are performed on-device. No personal details, scanner records, or images are uploaded to external servers.
2.  **Disclaimer of Liability**: The developer assumes no liability for missed appointments, lost work, delayed flights, or any other consequences resulting from missed alarms or failure of the app to trigger due to system-level restrictions.
