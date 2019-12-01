package com.momentolabs.app.security.applocker.ui.vault.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object VaultAnalytics {

    fun addedImageVault(context: Context, size: Int) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        when (size) {
            in 1..5 -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_IMAGE_VAULT_1_5, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_IMAGE_VAULT_1_5, size)
                })
            }
            in 5..10 -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_IMAGE_VAULT_5_10, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_IMAGE_VAULT_5_10, size)
                })
            }
            else -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_IMAGE_VAULT_10_MORE, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_IMAGE_VAULT_10_MORE, size)
                })
            }
        }
    }

    fun addedVideoVault(context: Context, size: Int) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        when (size) {
            in 1..5 -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_VIDEO_VAULT_1_5, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_VIDEO_VAULT_1_5, size)
                })
            }
            in 5..10 -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_VIDEO_VAULT_5_10, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_VIDEO_VAULT_5_10, size)
                })
            }
            else -> {
                firebaseAnalytics.logEvent(EVENT_NAME_ADDED_VIDEO_VAULT_10_MORE, Bundle().apply {
                    putInt(EVENT_NAME_ADDED_VIDEO_VAULT_10_MORE, size)
                })
            }
        }
    }

    fun removedImageVault(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_REMOVED_IMAGE_VAULT, Bundle().apply {
            putBoolean(EVENT_NAME_REMOVED_IMAGE_VAULT, true)
        })
    }

    fun removedVideoVault(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent(EVENT_NAME_REMOVED_VIDEO_VAULT, Bundle().apply {
            putBoolean(EVENT_NAME_REMOVED_VIDEO_VAULT, true)
        })
    }

    private const val EVENT_NAME_ADDED_IMAGE_VAULT_1_5 = "event_name_added_image_vault_1_5"
    private const val EVENT_NAME_ADDED_IMAGE_VAULT_5_10 = "event_name_added_image_vault_5_10"
    private const val EVENT_NAME_ADDED_IMAGE_VAULT_10_MORE = "event_name_added_image_vault_10_more"
    private const val EVENT_NAME_ADDED_VIDEO_VAULT_1_5 = "event_name_added_video_vault_1_5"
    private const val EVENT_NAME_ADDED_VIDEO_VAULT_5_10 = "event_name_added_video_vault_5_10"
    private const val EVENT_NAME_ADDED_VIDEO_VAULT_10_MORE = "event_name_added_video_vault_10_more"
    private const val EVENT_NAME_REMOVED_IMAGE_VAULT = "event_name_removed_image_vault"
    private const val EVENT_NAME_REMOVED_VIDEO_VAULT = "event_name_removed_image_vault"
}