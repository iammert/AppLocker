package com.momentolabs.app.security.applocker.ui.background.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object BackgroundAnalytics {

    fun sendBackgroundChangedEvent(context: Context, selectedId: Int) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_BACKGROUND, Bundle().apply {
            putInt(EVENT_NAME_BACKGROUND_ITEM_ID, selectedId)
        })
    }

    private const val EVENT_NAME_BACKGROUND = "background_changed"
    private const val EVENT_NAME_BACKGROUND_ITEM_ID = "background_changed_id"

}