package com.momentolabs.app.security.applocker.util.helper.file

import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaDao
import com.momentolabs.app.security.applocker.util.helper.ErrorLogger
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

/**
 * Moves vault files and intruder photos from the old folders in the root of the shared storage
 * to app specific storage, and updates the stored vault paths.
 *
 * Apps targeting Android 11 can no longer write to the shared storage root. Updated installs keep
 * their legacy access through preserveLegacyExternalStorage, which is enough to move the files
 * once. Files that cannot be read are left where they are.
 */
class LegacyStorageMigrator @Inject constructor(
    private val fileManager: FileManager,
    private val vaultMediaDao: VaultMediaDao
) {

    fun migrateInBackground() {
        Completable.fromAction { migrate() }
            .subscribeOn(Schedulers.io())
            .subscribe({}, ErrorLogger::log)
    }

    fun migrate() {
        migrateVault()
        moveFolderContents(FileManager.SubFolder.INTRUDERS)
    }

    private fun migrateVault() {
        val legacyFolder = fileManager.getLegacyDirectory(FileManager.SubFolder.VAULT)
        val newFolder = fileManager.getExternalDirectory(FileManager.SubFolder.VAULT)

        vaultMediaDao.getAllVaultMedia().forEach { media ->
            val encrypted = moveIfLegacy(media.encryptedPath, legacyFolder, newFolder)
            val preview = moveIfLegacy(media.encryptedPreviewPath, legacyFolder, newFolder)
            if (encrypted != media.encryptedPath || preview != media.encryptedPreviewPath) {
                vaultMediaDao.addToVault(media.copy(encryptedPath = encrypted, encryptedPreviewPath = preview))
            }
        }
    }

    private fun moveIfLegacy(path: String, legacyFolder: File, newFolder: File): String {
        val file = File(path)
        if (file.parentFile?.absolutePath != legacyFolder.absolutePath || file.canRead().not()) {
            return path
        }
        val target = File(newFolder, file.name)
        return if (move(file, target)) target.absolutePath else path
    }

    private fun moveFolderContents(subFolder: FileManager.SubFolder) {
        val legacyFolder = fileManager.getLegacyDirectory(subFolder)
        val newFolder = fileManager.getExternalDirectory(subFolder)
        legacyFolder.listFiles()?.filter { it.isFile && it.canRead() }?.forEach {
            move(it, File(newFolder, it.name))
        }
    }

    private fun move(source: File, target: File): Boolean {
        if (source.renameTo(target)) {
            return true
        }
        // renameTo does not work across storage volumes.
        return try {
            source.copyTo(target, overwrite = true)
            source.delete()
            true
        } catch (exception: Exception) {
            ErrorLogger.log(exception)
            target.delete()
            false
        }
    }
}
