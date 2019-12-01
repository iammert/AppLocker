package com.momentolabs.app.security.applocker.ui.settings

import android.content.Context
import com.momentolabs.app.security.applocker.R

data class FingerPrintStatusViewState(
    val isFingerPrintSupported: Boolean,
    val isFingerPrintRegistered: Boolean
) {

    fun getFingerPrintSettingTitle(context: Context): String {
        return when {
            isFingerPrintSupported.not() -> context.getString(R.string.setting_fingerprint_not_supported_title)
            isFingerPrintRegistered.not() -> context.getString(R.string.setting_fingerprint_not_registered_title)
            else -> context.getString(R.string.setting_fingerprint_title)
        }
    }

    fun getFingerPrintSettingSubtitle(context: Context): String {
        return when {
            isFingerPrintSupported.not() -> context.getString(R.string.setting_fingerprint_not_supported_description)
            isFingerPrintRegistered.not() -> context.getString(R.string.setting_fingerprint_not_registered_description)
            else -> context.getString(R.string.setting_fingerprint_description)
        }
    }

    fun isFingerPrintCheckBoxEnabled(): Boolean = isFingerPrintSupported && isFingerPrintRegistered
}