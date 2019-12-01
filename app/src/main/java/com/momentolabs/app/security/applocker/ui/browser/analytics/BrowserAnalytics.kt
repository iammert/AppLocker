package com.momentolabs.app.security.applocker.ui.browser.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


object BrowserAnalytics {

    fun sendQuickBrowsingClicked(context: Context, quickUrl: String) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_QUICK_BROWSING, Bundle().apply {
            putString(EVENT_NAME_QUICK_BROWSING_EVENT_KEY, quickUrl)
        })
    }

    private const val EVENT_NAME_QUICK_BROWSING = "browser_quick_browsing"
    private const val EVENT_NAME_QUICK_BROWSING_EVENT_KEY = "browser_quick_browsing_key"

}