package com.application.myalarm

import android.app.Application
import androidx.room.Room
import com.application.myalarm.data.db.AppDatabase
import com.application.myalarm.data.preferences.UserPreferences

class AlarmApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "myalarm_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val userPreferences: UserPreferences by lazy {
        UserPreferences(this)
    }
}
