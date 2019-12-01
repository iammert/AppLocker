package com.momentolabs.app.security.applocker.util.helper.file

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.ThumbnailUtils
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PreviewCreator @Inject constructor(private val fileManager: FileManager) {

    fun createPreviewImage(originalMediaFile: File, fileOperationRequest: FileOperationRequest): File {
        val destinationFile = fileManager.createFile(fileOperationRequest, FileManager.SubFolder.VAULT)

        val fileOutputStream = FileOutputStream(destinationFile)

        val image = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(originalMediaFile.absolutePath), 250, 250, MediaStore.Images.Thumbnails.MINI_KIND)

        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        } else {
            Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888).also {
                it.eraseColor(Color.GRAY)
                it.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            }
        }

        fileOutputStream.close()

        return destinationFile
    }

    fun createPreviewVideo(originalMediaFile: File, fileOperationRequest: FileOperationRequest): File {
        val destinationFile = fileManager.createFile(fileOperationRequest, FileManager.SubFolder.VAULT)

        val fileOutputStream = FileOutputStream(destinationFile)

        val image = ThumbnailUtils.createVideoThumbnail(originalMediaFile.absolutePath, MediaStore.Images.Thumbnails.MINI_KIND)

        if (image != null) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        } else {
            Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888).also {
                it.eraseColor(Color.GRAY)
                it.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            }
        }

        fileOutputStream.close()

        return destinationFile
    }
}