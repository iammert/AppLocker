package com.momentolabs.app.security.applocker.util.encryptor

import android.content.Context
import com.facebook.android.crypto.keychain.AndroidConceal
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain
import com.facebook.crypto.CryptoConfig
import com.facebook.crypto.Entity
import com.momentolabs.app.security.applocker.util.helper.file.FileManager
import com.momentolabs.app.security.applocker.util.helper.file.FileOperationRequest
import io.reactivex.Observable
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

class FileEncryptor @Inject constructor(context: Context, private val fileManager: FileManager) {

    private val appContext: Context = context.applicationContext

    private val keyChain = SharedPrefsBackedKeyChain(appContext, CryptoConfig.KEY_256)

    private val crypto = AndroidConceal.get().createDefaultCrypto(keyChain)

    fun encrypt(
            inputFile: File,
            encryptFileOperationRequest: EncryptFileOperationRequest
    ): Observable<CryptoProcess> {
        return Observable.create {
            it.onNext(CryptoProcess.processing(0))

            val outputFile = FileOperationRequest(
                    fileName = encryptFileOperationRequest.fileName,
                    directoryType = encryptFileOperationRequest.directoryType,
                    fileExtension = encryptFileOperationRequest.fileExtension
            ).run { fileManager.createFile(this, FileManager.SubFolder.VAULT) }


            val fileOutputStream = BufferedOutputStream(FileOutputStream(outputFile))
            val cryptoOutputStream =
                    crypto.getCipherOutputStream(fileOutputStream, Entity.create("entity_id"))

            val inputFileStream = FileInputStream(inputFile)

            val available = inputFileStream.available()
            val buf = ByteArray(1024)
            var read: Int = inputFileStream.read(buf)
            var totalRead = read

            while (read > 0) {

                cryptoOutputStream.write(buf, 0, read)
                read = inputFileStream.read(buf)

                totalRead += read
                val percent = totalRead * 100 / available
                it.onNext(CryptoProcess.processing(percent))
            }

            cryptoOutputStream.close()

            it.onNext(CryptoProcess.complete(outputFile))
            it.onComplete()
        }
    }

    fun previewEncrypt(inputFile: File, previewFile: File): Observable<CryptoProcess> {
        return Observable.create {
            val fileOutputStream = BufferedOutputStream(FileOutputStream(previewFile))
            val cryptoOutputStream =
                    crypto.getCipherOutputStream(fileOutputStream, Entity.create("entity_id"))
        }
    }

    fun decryptFile(
            inputFile: File,
            encryptFileOperationRequest: EncryptFileOperationRequest
    ): Observable<CryptoProcess> {
        return Observable.create {
            it.onNext(CryptoProcess.processing(0))

            val outputFile = FileOperationRequest(
                    fileName = encryptFileOperationRequest.fileName,
                    directoryType = encryptFileOperationRequest.directoryType,
                    fileExtension = encryptFileOperationRequest.fileExtension
            ).run { fileManager.createFile(this, FileManager.SubFolder.VAULT) }

            val fileOutputStream = FileOutputStream(outputFile)

            val fileInputStream = FileInputStream(inputFile)

            val fileCryptoInputStream =
                    crypto.getCipherInputStream(fileInputStream, Entity.create("entity_id"))

            val available = fileCryptoInputStream.available()
            val buffer = ByteArray(1024)
            var read: Int = fileCryptoInputStream.read(buffer)
            var totalRead = read

            while (read != -1) {
                fileOutputStream.write(buffer, 0, read)
                read = fileCryptoInputStream.read(buffer)

                totalRead += read
                val percent = totalRead * 100 / available
                it.onNext(CryptoProcess.processing(percent))
            }

            fileCryptoInputStream.close()
            it.onNext(CryptoProcess.complete(outputFile))
            it.onComplete()
        }
    }

    fun decryptFileWithFile(inputFile: File, outputFile: File): Observable<CryptoProcess> {
        return Observable.create {
            it.onNext(CryptoProcess.processing(0))

            val fileOutputStream = FileOutputStream(outputFile)

            val fileInputStream = FileInputStream(inputFile)

            val fileCryptoInputStream = crypto.getCipherInputStream(fileInputStream, Entity.create("entity_id"))

            val available = fileCryptoInputStream.available()
            val buffer = ByteArray(1024)
            var read: Int = fileCryptoInputStream.read(buffer)
            var totalRead: Long = read.toLong()

            while (read != -1) {
                fileOutputStream.write(buffer, 0, read)
                read = fileCryptoInputStream.read(buffer)

                totalRead += read
                val percent = (totalRead * 100 / available).toInt()
                it.onNext(CryptoProcess.processing(percent))
            }

            fileCryptoInputStream.close()
            it.onNext(CryptoProcess.complete(outputFile))
            it.onComplete()
        }
    }
}