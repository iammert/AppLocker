package com.momentolabs.app.security.applocker.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.util.helper.ErrorLogger

object ServiceStarter {

    /**
     * Starts [AppLockerService] as a foreground service.
     *
     * Since Android 12 a foreground service can only be started while the app is in the
     * foreground or in a few exempt cases such as BOOT_COMPLETED. Starts from the background, for
     * example from the periodic check, fail with an exception and are retried on the next run.
     */
    fun startService(context: Context) {
        try {
            ContextCompat.startForegroundService(context, Intent(context, AppLockerService::class.java))
        } catch (exception: IllegalStateException) {
            ErrorLogger.log(exception)
        }
    }
}
