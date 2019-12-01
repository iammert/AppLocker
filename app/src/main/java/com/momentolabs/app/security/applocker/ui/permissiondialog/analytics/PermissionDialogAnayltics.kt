package com.momentolabs.app.security.applocker.ui.permissiondialog.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object PermissionDialogAnayltics {

    fun usagePermissionPermitClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_USAGE_PERMISSION_PERMIT, Bundle().apply {
            putBoolean(EVENT_NAME_USAGE_PERMISSION_PERMIT, true)
        })
    }

    fun usagePermissionCancelClicked(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_USAGE_PERMISSION_CANCEL, Bundle().apply {
            putBoolean(EVENT_NAME_USAGE_PERMISSION_CANCEL, true)
        })
    }

    private const val EVENT_NAME_USAGE_PERMISSION_PERMIT = "usage_permission_permit_clicked"
    private const val EVENT_NAME_USAGE_PERMISSION_CANCEL = "usage_permission_cancel_clicked"

}