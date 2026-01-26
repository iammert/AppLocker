package com.momentolabs.app.security.applocker.util.helper.file

import android.content.Context
import android.os.Environment
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class FileManager @Inject constructor(val context: Context) {

    enum class SubFolder(val folderName: String) {
        VAULT(FOLDER_VAULT),
        INTRUDERS(FOLDER_INTRUDERS)
    }

    fun createFile(fileOperationRequest: FileOperationRequest, subFolder: SubFolder): File {
        val folder = when (fileOperationRequest.directoryType) {
            DirectoryType.CACHE -> getCacheDir()
            DirectoryType.EXTERNAL -> getExternalDirectory(subFolder)
        }

        return File(
            folder,
            fileOperationRequest.fileName + fileOperationRequest.fileExtension.extension
        )
    }

    fun deleteFile(filePath: String): Completable {
        return Completable.create {
            File(filePath).delete()
            it.onComplete()
        }
    }

    fun getFile(fileOperationRequest: FileOperationRequest, subFolder: SubFolder): File? {
        val folder = when (fileOperationRequest.directoryType) {
            DirectoryType.CACHE -> getCacheDir()
            DirectoryType.EXTERNAL -> getExternalDirectory(subFolder)
        }

        return File(folder.absolutePath + "/" + fileOperationRequest.fileName + fileOperationRequest.fileExtension.extension)
    }

    fun getSubFiles(folder: File, extension: FileExtension): List<File> {
        if (folder.isFile || folder.exists().not()) {
            return arrayListOf()
        }

        val files = folder.listFiles() ?: arrayOf()
        return files.filter { it.name.lowercase().endsWith(extension.extension.lowercase()) }
    }

    fun createFileInCache(fileName: String): File {
        return File(context.cacheDir, "$fileName.jpg")
    }

    fun isFileExist(filePath: String): Boolean {
        return File(filePath).exists()
    }

    fun isFileInCache(fileName: String): Boolean {
        val cacheFile = File(context.cacheDir, fileName)
        return cacheFile.exists()
    }

    /**
     * Returns the folder for [subFolder] in app specific storage. Encrypted vault files stay in
     * internal storage, intruder photos go to the app's external pictures folder so they can be
     * shared. Neither needs a storage permission.
     */
    fun getExternalDirectory(subFolder: SubFolder): File {
        val parent = when (subFolder) {
            SubFolder.VAULT -> context.filesDir
            SubFolder.INTRUDERS -> context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        }
        return File(parent, subFolder.folderName).apply { mkdirs() }
    }

    /** Folder used before 1.6.0, in the root of the shared storage. */
    fun getLegacyDirectory(subFolder: SubFolder): File =
        File(Environment.getExternalStorageDirectory(), "applocker/${subFolder.folderName}")

    private fun getCacheDir(): File = context.cacheDir

    companion object {
        private const val FOLDER_VAULT = "vault"
        private const val FOLDER_INTRUDERS = "intruders"
    }
}