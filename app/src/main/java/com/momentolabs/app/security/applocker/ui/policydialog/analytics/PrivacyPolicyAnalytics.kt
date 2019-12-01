package com.momentolabs.app.security.applocker.ui.policydialog.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object PrivacyPolicyAnalytics {

    fun sendPrivacyPolicyAccept(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_PRIVACY_POLICY,Bundle().apply {
            putBoolean(EVENT_NAME_PRIVACY_POLICY, true)
        })
    }

    private const val EVENT_NAME_PRIVACY_POLICY = "privacy_policy_accept"

}