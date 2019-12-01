package com.momentolabs.app.security.applocker.ui.main.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object MainActivityAnalytics {

    fun onSecurityTabSelected(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_TAB_SECURITY_SELECT, Bundle().apply {
            putBoolean(EVENT_NAME_TAB_SECURITY_SELECT, true)
        })
    }

    fun onSettingsTabSelected(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_TAB_SETTINGS_SELECT, Bundle().apply {
            putBoolean(EVENT_NAME_TAB_SETTINGS_SELECT, true)
        })
    }

    private const val EVENT_NAME_TAB_SECURITY_SELECT = "tab_security_selected"
    private const val EVENT_NAME_TAB_SETTINGS_SELECT = "tab_settings_selected"

}