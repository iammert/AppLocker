package com.momentolabs.app.security.applocker.ui.security

import com.momentolabs.app.security.applocker.data.AppData
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.ui.security.function.LockedAppListViewStateCreator
import org.junit.Assert.assertEquals
import org.junit.Test

class LockedAppListViewStateCreatorTest {

    @Test
    fun marksOnlyLockedAppsAsLocked() {
        val installed = listOf(app("com.a/Main"), app("com.b/Main"), app("com.c/Main"))
        val locked = listOf(LockedAppEntity(packageName = "com.b/Main"), LockedAppEntity(packageName = "com.removed/Main"))

        val viewStates = LockedAppListViewStateCreator().apply(installed, locked)

        assertEquals(listOf(false, true, false), viewStates.map { it.isLocked })
        assertEquals(installed, viewStates.map { it.appData })
    }

    private fun app(packageName: String) = AppData(appName = packageName, packageName = packageName, appIconDrawable = null)
}
