package com.momentolabs.app.security.applocker.ui.vault.vaultlist

import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity

data class VaultListItemViewState(val vaultMediaEntity: VaultMediaEntity) {

    fun getDecryptedCachePath() = vaultMediaEntity.decryptedPreviewCachePath
}