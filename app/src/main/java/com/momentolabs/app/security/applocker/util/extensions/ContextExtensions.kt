package com.momentolabs.app.security.applocker.util.extensions

import android.content.Context
import androidx.core.os.ConfigurationCompat.getLocales
import android.os.Build
import java.util.*


fun Context.getCurrentLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
    } else {
        resources.configuration.locale
    }
}