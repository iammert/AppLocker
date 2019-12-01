package com.momentolabs.app.security.applocker.ui.vault.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object VaultAdAnalytics {

    fun bannerAdLoaded(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_AD_LOADED, Bundle().apply {
            putBoolean(EVENT_NAME_AD_LOADED, true)
        })
    }

    fun bannerAdFailed(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_AD_FAILED, Bundle().apply {
            putBoolean(EVENT_NAME_AD_FAILED, true)
        })
    }

    fun bannerAdClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_AD_CLICKED, Bundle().apply {
            putBoolean(EVENT_NAME_AD_CLICKED, true)
        })
    }

    private const val EVENT_NAME_AD_LOADED = "ads_vault_banner_loaded"
    private const val EVENT_NAME_AD_FAILED = "ads_vault_banner_failed"
    private const val EVENT_NAME_AD_CLICKED = "ads_vault_banner_clicked"
}