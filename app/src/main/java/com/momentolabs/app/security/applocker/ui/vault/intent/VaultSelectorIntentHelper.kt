package com.momentolabs.app.security.applocker.ui.vault.intent

import android.content.Intent

object VaultSelectorIntentHelper {

    fun selectVideoIntent(): Intent {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        return Intent.createChooser(intent, "Select Video")
    }

    fun selectMultipleImageIntent(): Intent {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        return Intent.createChooser(intent, "Select Image")
    }
}