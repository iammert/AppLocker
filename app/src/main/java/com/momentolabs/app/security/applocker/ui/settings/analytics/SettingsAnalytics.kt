package com.momentolabs.app.security.applocker.ui.settings.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object SettingsAnalytics {

    fun fingerPrintEnabled(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_FINGERPRINT_ENABLED, Bundle().apply {
            putBoolean(EVENT_NAME_FINGERPRINT_ENABLED, true)
        })
    }

    fun intrudersEnabled(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_INTRUDERS_ENABLED, Bundle().apply {
            putBoolean(EVENT_NAME_INTRUDERS_ENABLED, true)
        })
    }

    fun intrudersFolderClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_INTRUDERS_FOLDER_CLICKED, Bundle().apply {
            putBoolean(EVENT_NAME_INTRUDERS_FOLDER_CLICKED, true)
        })
    }

    private const val EVENT_NAME_FINGERPRINT_ENABLED = "event_name_fingerprint_enabled"
    private const val EVENT_NAME_INTRUDERS_ENABLED = "event_name_intruders_enabled"
    private const val EVENT_NAME_INTRUDERS_FOLDER_CLICKED = "event_name_intruders_folder_clicked"

}