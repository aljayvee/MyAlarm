package com.application.myalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.application.myalarm.ui.navigation.AppNavigation
import com.application.myalarm.ui.theme.MyAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAlarmTheme {
                AppNavigation()
            }
        }
    }
}