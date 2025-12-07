package com.momentolabs.app.security.applocker.util.helper

import android.util.Log

/**
 * Logs unexpected errors. Hook a crash reporter in here if a build needs one.
 */
object ErrorLogger {

    private const val TAG = "AppLocker"

    fun log(throwable: Throwable?) {
        Log.e(TAG, throwable?.message ?: "Unexpected error", throwable)
    }
}
