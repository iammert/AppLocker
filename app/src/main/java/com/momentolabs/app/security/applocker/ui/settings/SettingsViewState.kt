package com.momentolabs.app.security.applocker.ui.settings

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.R

data class SettingsViewState(
    val isAllAppLocked: Boolean = false,
    val isHiddenDrawingMode: Boolean = false,
    val isFingerPrintEnabled: Boolean = false,
    val isIntrudersCatcherEnabled: Boolean = false
) {

    fun getLockAllAppsIcon(context: Context): Drawable? {
        return if (isAllAppLocked) {
            ContextCompat.getDrawable(context, R.drawable.ic_locked_24px)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_lock_open_24px)
        }
    }

    fun lockAllAppsTitle(context: Context): String =
        if (isAllAppLocked) {
            context.getString(R.string.title_unlock_all)
        } else {
            context.getString(R.string.title_lock_all)
        }

    fun lockAllAppsDescription(context: Context) =
        if (isAllAppLocked) {
            context.getString(R.string.description_unlock_all)
        } else {
            context.getString(R.string.description_lock_all)
        }

}