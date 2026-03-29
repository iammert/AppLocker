package com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog

import android.content.Context
import android.net.Uri
import com.momentolabs.app.security.applocker.R

/**
 * @param undeletedMediaUris originals that are now in the vault but could not be deleted
 * directly, because Android 11 and later only let the user confirm their deletion.
 */
data class AddToVaultViewState(
    val progress: Int,
    val processState: ProcessState,
    val undeletedMediaUris: List<Uri> = emptyList()
) {

    fun getPercentText(context: Context): String {
        return context.getString(R.string.dialog_action_process, progress)
    }
}