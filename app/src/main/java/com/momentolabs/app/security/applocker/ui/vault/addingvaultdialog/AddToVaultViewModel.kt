package com.momentolabs.app.security.applocker.ui.vault.addingvaultdialog

import com.momentolabs.app.security.applocker.util.helper.ErrorLogger
import android.app.Application
import android.net.Uri
import android.os.Build
import com.momentolabs.app.security.applocker.util.helper.file.MediaStoreHelper
import java.io.File
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.repository.VaultRepository
import com.momentolabs.app.security.applocker.ui.RxAwareAndroidViewModel
import com.momentolabs.app.security.applocker.util.encryptor.CryptoProcess
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import com.momentolabs.app.security.applocker.util.helper.file.FileManager
import com.momentolabs.app.security.applocker.util.helper.file.MediaScannerConnector
import com.momentolabs.app.security.applocker.util.helper.progress.FakeProgress
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddToVaultViewModel @Inject constructor(
    private val app: Application,
    private val vaultRepository: VaultRepository,
    private val fileManager: FileManager
) : RxAwareAndroidViewModel(app) {

    private val fakeProgress = FakeProgress()

    private val undeletedMediaUris = mutableListOf<Uri>()

    private val addToVaultViewStateLiveData = MutableLiveData<AddToVaultViewState>()
        .apply {
            value = AddToVaultViewState(progress = 0, processState = ProcessState.PROCESSING)
        }

    init {
        with(fakeProgress) {
            setOnProgressListener {
                addToVaultViewStateLiveData.value = AddToVaultViewState(it, ProcessState.PROCESSING)
            }

            setOnCompletedListener {
                addToVaultViewStateLiveData.value = AddToVaultViewState(
                    100,
                    ProcessState.COMPLETE,
                    synchronized(undeletedMediaUris) { undeletedMediaUris.toList() }
                )
            }
        }
    }

    fun setSelectedFilePath(selectedFilePath: ArrayList<String>, mediaType: VaultMediaType) {

        val list = arrayListOf<Observable<CryptoProcess>>()

        for (selected in selectedFilePath) {
            val encryptionObservable = vaultRepository
                .addMediaToVault(selected, mediaType)
                .doOnNext {
                    if (it is CryptoProcess.Complete) {
                        removeOriginal(selected)
                    }
                }

            list.add(encryptionObservable)
        }
        disposables += Observable.merge(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { fakeProgress.start() }
            .doOnComplete { fakeProgress.complete() }
            .subscribe({}, { ErrorLogger.log(it) })
    }

    fun getAddToVaultViewStateLiveData(): LiveData<AddToVaultViewState> =
        addToVaultViewStateLiveData

    /**
     * Deletes the original file once it is encrypted. Since Android 11 other apps' media can only
     * be deleted after the user confirms, so those files are collected for a delete request.
     */
    private fun removeOriginal(filePath: String) {
        val file = File(filePath)
        if (file.delete() || file.exists().not()) {
            MediaScannerConnector.scan(context = app, filePath = filePath)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            MediaStoreHelper.findMediaUri(app, filePath)?.let {
                synchronized(undeletedMediaUris) { undeletedMediaUris.add(it) }
            }
        }
    }
}