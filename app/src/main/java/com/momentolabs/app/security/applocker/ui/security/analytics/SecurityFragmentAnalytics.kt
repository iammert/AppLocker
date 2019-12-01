package com.momentolabs.app.security.applocker.ui.security.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object SecurityFragmentAnalytics {

    fun onAppLocked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_APP_LOCKED, Bundle().apply {
            putBoolean(EVENT_NAME_APP_LOCKED, true)
        })
    }

    fun onAppUnlocked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_APP_UNLOCKED, Bundle().apply {
            putBoolean(EVENT_NAME_APP_UNLOCKED, true)
        })
    }

    fun onBrowserClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_BROWSER, Bundle().apply {
            putBoolean(EVENT_NAME_BROWSER, true)
        })
    }

    fun onBackgroundClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_BACKGROUND, Bundle().apply {
            putBoolean(EVENT_NAME_BACKGROUND, true)
        })
    }

    fun onCallBlockerClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_CALL_BLOCKER, Bundle().apply {
            putBoolean(EVENT_NAME_CALL_BLOCKER, true)
        })
    }

    fun onVaultClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_VAULT, Bundle().apply {
            putBoolean(EVENT_NAME_VAULT, true)
        })
    }

    private const val EVENT_NAME_APP_LOCKED = "app_item_locked"
    private const val EVENT_NAME_APP_UNLOCKED = "app_item_unlocked"

    private const val EVENT_NAME_BROWSER = "browser_clicked"
    private const val EVENT_NAME_BACKGROUND = "background_clicked"
    private const val EVENT_NAME_VAULT = "vault_clicked"
    private const val EVENT_NAME_CALL_BLOCKER = "call_blocker_clicked"

}
