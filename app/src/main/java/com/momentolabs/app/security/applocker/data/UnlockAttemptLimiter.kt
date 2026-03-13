package com.momentolabs.app.security.applocker.data

import android.content.Context
import android.os.SystemClock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Locks pattern input for [LOCKOUT_MILLIS] after [MAX_FAILED_ATTEMPTS] wrong patterns in a row.
 *
 * The state is shared by the lock screen overlay and the lock activity and survives a restart of
 * the service. The lockout end is based on the elapsed realtime, so changing the clock does not
 * end it early; a reboot does.
 */
class UnlockAttemptLimiter @Inject constructor(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun remainingLockoutSeconds(): Int {
        val remaining = preferences.getLong(KEY_LOCKOUT_UNTIL, 0) - SystemClock.elapsedRealtime()
        return if (remaining > 0 && remaining <= LOCKOUT_MILLIS) {
            TimeUnit.MILLISECONDS.toSeconds(remaining + 999).toInt()
        } else {
            0
        }
    }

    /** Records a wrong pattern and returns the remaining lockout in seconds, 0 if none. */
    fun onFailedAttempt(): Int {
        val failedAttempts = preferences.getInt(KEY_FAILED_ATTEMPTS, 0) + 1
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            preferences.edit()
                .putInt(KEY_FAILED_ATTEMPTS, 0)
                .putLong(KEY_LOCKOUT_UNTIL, SystemClock.elapsedRealtime() + LOCKOUT_MILLIS)
                .apply()
        } else {
            preferences.edit().putInt(KEY_FAILED_ATTEMPTS, failedAttempts).apply()
        }
        return remainingLockoutSeconds()
    }

    fun onSuccessfulAttempt() {
        preferences.edit().clear().apply()
    }

    companion object {
        const val MAX_FAILED_ATTEMPTS = 5
        val LOCKOUT_MILLIS = TimeUnit.SECONDS.toMillis(30)

        private const val PREFERENCES_NAME = "UnlockAttemptLimiter"
        private const val KEY_FAILED_ATTEMPTS = "KEY_FAILED_ATTEMPTS"
        private const val KEY_LOCKOUT_UNTIL = "KEY_LOCKOUT_UNTIL"
    }
}
