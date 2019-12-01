package com.momentolabs.app.security.applocker.service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat


object ServiceStarter {

    fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, Intent(context, AppLockerService::class.java))
        } else {
            context.startService(Intent(context, AppLockerService::class.java))
        }
    }
}