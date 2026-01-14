package com.momentolabs.app.security.applocker.util.encryptor

import android.content.Context
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.momentolabs.app.security.applocker.util.helper.file.FileManager
import com.momentolabs.app.security.applocker.util.helper.file.FileOperationRequest
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class FileEncryptor @Inject constructor(context: Context, private val fileManager: FileManager) {

    private val appContext: Context = context.applicationContext

    private val keyChain = SharedPrefsBackedKeyChain(appContext, CryptoConfig.KEY_256)

    private val crypto = AndroidConceal.get().createDefaultCrypto(keyChain)

    private val entity = Entity.create(ENTITY_ID)

    fun encrypt(
        inputFile: File,
        encryptFileOperationRequest: EncryptFileOperationRequest
    ): Observable<CryptoProcess> {
        return Observable.create { emitter ->
            emitter.onNext(CryptoProcess.processing(0))

            val outputFile = createOutputFile(encryptFileOperationRequest)
            BufferedInputStream(FileInputStream(inputFile)).use { input ->
                crypto.getCipherOutputStream(BufferedOutputStream(FileOutputStream(outputFile)), entity)
                    .use { output -> copyWithProgress(input, output, inputFile.length(), emitter) }
            }

            emitter.onNext(CryptoProcess.complete(outputFile))
            emitter.onComplete()
        }
    }

    fun decryptFile(
        inputFile: File,
        encryptFileOperationRequest: EncryptFileOperationRequest
    ): Observable<CryptoProcess> {
        return Observable.defer {
            decryptFileWithFile(inputFile, createOutputFile(encryptFileOperationRequest))
        }
    }

    fun decryptFileWithFile(inputFile: File, outputFile: File): Observable<CryptoProcess> {
        return Observable.create { emitter ->
            emitter.onNext(CryptoProcess.processing(0))

            crypto.getCipherInputStream(BufferedInputStream(FileInputStream(inputFile)), entity).use { input ->
                BufferedOutputStream(FileOutputStream(outputFile)).use { output ->
                    // The encrypted file is slightly larger than the plain one, which is close
                    // enough for a progress value.
                    copyWithProgress(input, output, inputFile.length(), emitter)
                }
            }

            emitter.onNext(CryptoProcess.complete(outputFile))
            emitter.onComplete()
        }
    }

    private fun createOutputFile(request: EncryptFileOperationRequest): File =
        FileOperationRequest(
            fileName = request.fileName,
            directoryType = request.directoryType,
            fileExtension = request.fileExtension
        ).run { fileManager.createFile(this, FileManager.SubFolder.VAULT) }

    /**
     * Copies [input] to [output] and emits the progress, only when the percentage changes.
     */
    private fun copyWithProgress(
        input: InputStream,
        output: OutputStream,
        totalBytes: Long,
        emitter: ObservableEmitter<CryptoProcess>
    ) {
        val buffer = ByteArray(BUFFER_SIZE)
        var copiedBytes = 0L
        var lastPercentage = 0
        while (emitter.isDisposed.not()) {
            val read = input.read(buffer)
            if (read == -1) {
                break
            }
            output.write(buffer, 0, read)
            copiedBytes += read

            val percentage = if (totalBytes > 0) {
                (copiedBytes * 100 / totalBytes).toInt().coerceAtMost(100)
            } else {
                0
            }
            if (percentage != lastPercentage) {
                lastPercentage = percentage
                emitter.onNext(CryptoProcess.processing(percentage))
            }
        }
    }

    companion object {
        private const val ENTITY_ID = "entity_id"
        private const val BUFFER_SIZE = 64 * 1024
    }
}
