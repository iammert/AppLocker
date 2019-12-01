package com.momentolabs.app.security.applocker.ui.overlay.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object OverlayAnalytics {

    fun sendIntrudersPhotoTakenEvent(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_INTRUDERS_CATCHED, Bundle().apply {
            putBoolean(EVENT_NAME_INTRUDERS_CATCHED, true)
        })
    }

    fun sendIntrudersCameraFailedEvent(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_INTRUDERS_CAMERA_FAILED, Bundle().apply {
            putBoolean(EVENT_NAME_INTRUDERS_CAMERA_FAILED, true)
        })
    }

    private const val EVENT_NAME_INTRUDERS_CATCHED = "EVENT_NAME_INTRUDERS_CATCHED"
    private const val EVENT_NAME_INTRUDERS_CAMERA_FAILED = "EVENT_NAME_INTRUDERS_CAMERA_FAILED"
}