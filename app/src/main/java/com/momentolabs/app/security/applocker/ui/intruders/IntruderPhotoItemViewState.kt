package com.momentolabs.app.security.applocker.ui.intruders

import com.momentolabs.app.security.applocker.util.binding.ImageSize
import java.io.File

data class IntruderPhotoItemViewState(val file: File) {

    fun getFilePath() = file.absolutePath

    fun getImageSize(): ImageSize = ImageSize.MEDIUM
}