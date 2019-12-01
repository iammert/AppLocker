package com.momentolabs.app.security.applocker.util.helper.file

data class FileOperationRequest(
    val fileName: String,
    val fileExtension: FileExtension,
    val directoryType: DirectoryType
)