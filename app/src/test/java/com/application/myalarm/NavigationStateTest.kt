package com.application.myalarm

import com.application.myalarm.ui.navigation.AppNavigationState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class NavigationStateTest {

    @Test
    fun testInitialState() {
        val state = AppNavigationState()
        assertEquals("home", state.currentRoute)
        assertEquals(1, state.backStack.size)
        assertEquals("home", state.backStack.first())
    }

    @Test
    fun testNavigate() {
        val state = AppNavigationState()
        state.navigate("settings")
        assertEquals("settings", state.currentRoute)
        assertEquals(2, state.backStack.size)
        assertEquals("home", state.backStack[0])
        assertEquals("settings", state.backStack[1])

        state.navigate("terms")
        assertEquals("terms", state.currentRoute)
        assertEquals(3, state.backStack.size)
        assertEquals("terms", state.backStack[2])
    }

    @Test
    fun testNavigateBack() {
        val state = AppNavigationState()
        state.navigate("settings")
        state.navigate("terms")
        
        state.navigateBack()
        assertEquals("settings", state.currentRoute)
        assertEquals(2, state.backStack.size)

        state.navigateBack()
        assertEquals("home", state.currentRoute)
        assertEquals(1, state.backStack.size)

        // Try navigating back from the root, it should not pop further
        state.navigateBack()
        assertEquals("home", state.currentRoute)
        assertEquals(1, state.backStack.size)
    }

    @Test
    fun testSelectTab() {
        val state = AppNavigationState()
        state.navigate("settings")
        state.navigate("terms")

        state.selectTab("alarms")
        assertEquals("alarms", state.currentRoute)
        assertEquals(1, state.backStack.size)
        assertEquals("alarms", state.backStack[0])
    }

    @Test
    fun testSaverSaveAndRestore() {
        val state = AppNavigationState()
        state.navigate("settings")
        state.navigate("terms")

        // Save using listSaver
        val savedObject = with(AppNavigationState.Saver) {
            val scope = androidx.compose.runtime.saveable.SaverScope { true }
            scope.save(state)
        }

        assertNotNull(savedObject)
        @Suppress("UNCHECKED_CAST")
        val savedList = savedObject as List<String>
        
        assertEquals(3, savedList.size)
        assertEquals("home", savedList[0])
        assertEquals("settings", savedList[1])
        assertEquals("terms", savedList[2])

        // Restore
        val restoredState = AppNavigationState.Saver.restore(savedList)
        assertNotNull(restoredState)
        assertEquals("terms", restoredState!!.currentRoute)
        assertEquals(3, restoredState.backStack.size)
        assertEquals("home", restoredState.backStack[0])
        assertEquals("settings", restoredState.backStack[1])
        assertEquals("terms", restoredState.backStack[2])
    }
}
