package com.application.myalarm.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

class AppNavigationState(
    initialRoute: String = "home",
    initialBackStack: List<String> = listOf("home")
) {
    var currentRoute by mutableStateOf(initialRoute)
        internal set

    val backStack: SnapshotStateList<String> = mutableStateListOf<String>().apply {
        addAll(initialBackStack)
    }

    fun navigate(route: String) {
        backStack.add(route)
        currentRoute = route
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
            currentRoute = backStack.last()
        }
    }

    fun selectTab(route: String) {
        backStack.clear()
        backStack.add(route)
        currentRoute = route
    }

    companion object {
        val Saver = listSaver<AppNavigationState, String>(
            save = { state ->
                state.backStack.toList()
            },
            restore = { list ->
                val lastRoute = list.lastOrNull() ?: "home"
                AppNavigationState(
                    initialRoute = lastRoute,
                    initialBackStack = list
                )
            }
        )
    }
}
