package com.momentolabs.app.security.applocker.ui.settings

import com.momentolabs.app.security.applocker.data.AppData
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IsAllAppsLockedStateCreatorTest {

    private val creator = IsAllAppsLockedStateCreator()

    private val installed = listOf(app("com.a/Main"), app("com.b/Main"))

    @Test
    fun allInstalledAppsLocked() {
        assertTrue(creator.apply(installed, listOf(locked("com.a/Main"), locked("com.b/Main"))))
    }

    @Test
    fun uninstalledLockedAppDoesNotCountAsLocked() {
        assertFalse(creator.apply(installed, listOf(locked("com.a/Main"), locked("com.removed/Main"))))
    }

    @Test
    fun extraLockedAppsAreIgnored() {
        assertTrue(
            creator.apply(installed, listOf(locked("com.a/Main"), locked("com.b/Main"), locked("com.removed/Main")))
        )
    }

    @Test
    fun noInstalledAppsIsNotAllLocked() {
        assertFalse(creator.apply(emptyList(), emptyList()))
    }

    private fun app(packageName: String) = AppData(appName = packageName, packageName = packageName, appIconDrawable = null)

    private fun locked(packageName: String) = LockedAppEntity(packageName = packageName)
}
