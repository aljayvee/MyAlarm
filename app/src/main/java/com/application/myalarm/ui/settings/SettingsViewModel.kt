package com.application.myalarm.ui.settings

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.myalarm.AlarmApplication
import com.application.myalarm.util.Localizer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AlarmApplication
    private val userPrefs = app.userPreferences

    private val _notificationPermissionGranted = MutableStateFlow(checkPermission(application))
    val notificationPermissionGranted: StateFlow<Boolean> = _notificationPermissionGranted.asStateFlow()

    private val _overlayPermissionGranted = MutableStateFlow(checkOverlayPermissionInternal(application))
    val overlayPermissionGranted: StateFlow<Boolean> = _overlayPermissionGranted.asStateFlow()

    val selectedLanguage: StateFlow<String> = userPrefs.selectedLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    fun checkNotificationPermission(context: Context) {
        _notificationPermissionGranted.value = checkPermission(context)
    }

    fun checkOverlayPermission(context: Context) {
        _overlayPermissionGranted.value = checkOverlayPermissionInternal(context)
    }

    fun updateLanguage(langCode: String) {
        viewModelScope.launch {
            userPrefs.updateSelectedLanguage(langCode)
            // Dynamically update Localizer
            val matchedLang = Localizer.Language.values().find { it.code == langCode } ?: Localizer.Language.ENGLISH
            Localizer.currentLanguage = matchedLang
        }
    }

    private fun checkPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun checkOverlayPermissionInternal(context: Context): Boolean {
        return android.provider.Settings.canDrawOverlays(context)
    }
}
