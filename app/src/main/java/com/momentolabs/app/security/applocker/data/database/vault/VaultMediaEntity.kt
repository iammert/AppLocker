package com.momentolabs.app.security.applocker.data.database.vault

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "vault_media")
data class VaultMediaEntity(
    @PrimaryKey @ColumnInfo(name = "original_path") val originalPath: String,
    @ColumnInfo(name = "original_file_name") val originalFileName: String = "",
    @ColumnInfo(name = "encrypted_path") val encryptedPath: String = "",
    @ColumnInfo(name = "encrypted_preview_path") val encryptedPreviewPath: String = "",
    @ColumnInfo(name = "media_type") val mediaType: String = ""
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var decryptedPreviewCachePath: String = ""

    fun getEncryptedPreviewFileName(): String {
        val slashIndex = encryptedPreviewPath.lastIndexOf('/')
        return encryptedPreviewPath.substring(slashIndex + 1)
    }
}