package com.momentolabs.app.security.applocker.ui.permissions

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import android.provider.Settings

object PermissionChecker {

    fun checkUsageAccessPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return true
        }

        return try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0

            mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid, applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED

        } catch (e: NameNotFoundException) {
            false
        }

    }

    fun checkOverlayPermission(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }

    fun isAllPermissionChecked(context: Context) =
        checkUsageAccessPermission(context) && checkOverlayPermission(context)
}