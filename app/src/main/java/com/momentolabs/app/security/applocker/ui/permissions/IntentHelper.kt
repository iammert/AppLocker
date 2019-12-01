package com.momentolabs.app.security.applocker.ui.permissions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings


object IntentHelper {

    fun overlayIntent(packageName: String): Intent {
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
    }

    fun usageAccessIntent(): Intent {
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    }

    fun privacyPolicyWebIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("http://bit.ly/2UOmxEy"))
    }

    fun rateUsIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.momentolabs.app.security.applocker"))
    }

    fun startStorePage(activity: Activity) {
        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Momento Labs")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            activity.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Momento Labs"))
            )
        }
    }
}