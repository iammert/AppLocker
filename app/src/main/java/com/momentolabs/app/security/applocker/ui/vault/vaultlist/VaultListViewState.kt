package com.momentolabs.app.security.applocker.ui.vault.vaultlist

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType.*

data class VaultListViewState(val vaultMediaType: VaultMediaType, val vaultList: List<VaultMediaEntity>) {

    fun getEmptyPageVisibility(): Int {
        return if (vaultList.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    fun getEmptyPageDrawable(context: Context): Drawable? {
        return when (vaultMediaType) {
            TYPE_IMAGE -> ContextCompat.getDrawable(context, R.drawable.ic_vault_image_empty)
            TYPE_VIDEO -> ContextCompat.getDrawable(context, R.drawable.ic_vault_video_empty)
        }
    }

    fun getEmptyPageTitle(context: Context): String {
        return when (vaultMediaType) {
            TYPE_IMAGE -> context.getString(R.string.vault_photo_empty_page_title)
            TYPE_VIDEO -> context.getString(R.string.vault_video_empty_page_title)
        }
    }

    fun getEmptyPageDescription(context: Context): String {
        return when (vaultMediaType) {
            TYPE_IMAGE -> context.getString(R.string.vault_photo_empty_page_description)
            TYPE_VIDEO -> context.getString(R.string.vault_video_empty_page_description)
        }
    }

    companion object {

        fun empty(vaultMediaType: VaultMediaType) =
            VaultListViewState(vaultMediaType = vaultMediaType, vaultList = arrayListOf())
    }

}