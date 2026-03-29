package com.momentolabs.app.security.applocker.util.helper.file

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object MediaStoreHelper {

    /** Finds the MediaStore entry of a file in the shared storage, or null if it has none. */
    @Suppress("DEPRECATION")
    fun findMediaUri(context: Context, filePath: String): Uri? {
        val collection = MediaStore.Files.getContentUri("external")
        return context.contentResolver.query(
            collection,
            arrayOf(MediaStore.MediaColumns._ID),
            "${MediaStore.MediaColumns.DATA} = ?",
            arrayOf(filePath),
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) ContentUris.withAppendedId(collection, cursor.getLong(0)) else null
        }
    }
}
