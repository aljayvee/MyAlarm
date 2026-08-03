package com.application.myalarm.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val LAST_DISMISS_DATE = stringPreferencesKey("last_dismiss_date")
        val NOTIFICATION_PERMISSION_ASKED = booleanPreferencesKey("notification_permission_asked")
        val LOCK_SCREEN_PERMISSION_ASKED = booleanPreferencesKey("lock_screen_permission_asked")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_VERSION_CODE = intPreferencesKey("last_version_code")
        val DEFAULT_MISSION_TYPE = stringPreferencesKey("default_mission_type")
    }

    val currentStreak: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[CURRENT_STREAK] ?: 0
    }

    val bestStreak: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[BEST_STREAK] ?: 0
    }

    val lastDismissDate: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[LAST_DISMISS_DATE]
    }

    val notificationPermissionAsked: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[NOTIFICATION_PERMISSION_ASKED] ?: false
    }

    val lockScreenPermissionAsked: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[LOCK_SCREEN_PERMISSION_ASKED] ?: false
    }

    val selectedLanguage: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_LANGUAGE] ?: "en"
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETED] ?: false
    }

    val lastVersionCode: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[LAST_VERSION_CODE] ?: 0
    }

    val defaultMissionType: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[DEFAULT_MISSION_TYPE] ?: "MATH_PROBLEM"
    }

    suspend fun updateStreak(newStreak: Int) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_STREAK] = newStreak
        }
    }

    suspend fun updateBestStreak(newBest: Int) {
        context.dataStore.edit { prefs ->
            prefs[BEST_STREAK] = newBest
        }
    }

    suspend fun updateLastDismissDate(date: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_DISMISS_DATE] = date
        }
    }

    suspend fun setNotificationPermissionAsked(asked: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATION_PERMISSION_ASKED] = asked
        }
    }

    suspend fun setLockScreenPermissionAsked(asked: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[LOCK_SCREEN_PERMISSION_ASKED] = asked
        }
    }

    suspend fun updateSelectedLanguage(langCode: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_LANGUAGE] = langCode
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun updateLastVersionCode(code: Int) {
        context.dataStore.edit { prefs ->
            prefs[LAST_VERSION_CODE] = code
        }
    }

    suspend fun updateDefaultMissionType(missionType: String) {
        context.dataStore.edit { prefs ->
            prefs[DEFAULT_MISSION_TYPE] = missionType
        }
    }
}
