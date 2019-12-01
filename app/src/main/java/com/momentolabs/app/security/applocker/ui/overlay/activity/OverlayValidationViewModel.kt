package com.momentolabs.app.security.applocker.ui.overlay.activity

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.data.AppLockerPreferences
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDao
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDot
import com.momentolabs.app.security.applocker.service.PatternValidatorFunction
import com.momentolabs.app.security.applocker.ui.RxAwareAndroidViewModel
import com.momentolabs.app.security.applocker.ui.background.GradientBackgroundDataProvider
import com.momentolabs.app.security.applocker.ui.background.GradientItemViewState
import com.momentolabs.app.security.applocker.ui.overlay.OverlayValidateType
import com.momentolabs.app.security.applocker.ui.overlay.OverlayViewState
import com.momentolabs.app.security.applocker.ui.overlay.activity.FingerPrintResult.*
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import com.momentolabs.app.security.applocker.util.helper.file.DirectoryType
import com.momentolabs.app.security.applocker.util.helper.file.FileExtension
import com.momentolabs.app.security.applocker.util.helper.file.FileManager
import com.momentolabs.app.security.applocker.util.helper.file.FileOperationRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject

class OverlayValidationViewModel @Inject constructor(
    val app: Application,
    val patternDao: PatternDao,
    val appLockerPreferences: AppLockerPreferences,
    val fileManager: FileManager
) : RxAwareAndroidViewModel(app) {

    private val patternValidationViewStateLiveData = MediatorLiveData<OverlayViewState>()
        .apply {
            this.value = OverlayViewState(
                isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
                isFingerPrintMode = appLockerPreferences.getFingerPrintEnabled()
            )
        }

    private val selectedBackgroundDrawableLiveData = MutableLiveData<GradientItemViewState>()

    private val fingerPrintLiveData = FingerPrintLiveData(getApplication())

    private val patternDrawnSubject = PublishSubject.create<List<PatternDot>>()

    init {
        val existingPatternObservable = patternDao.getPattern().map { it.patternMetadata.pattern }
        disposables += Flowable
            .combineLatest(
                existingPatternObservable,
                patternDrawnSubject.toFlowable(BackpressureStrategy.BUFFER),
                PatternValidatorFunction()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isValidated ->
                patternValidationViewStateLiveData.value =
                    OverlayViewState(
                        overlayValidateType = OverlayValidateType.TYPE_PATTERN,
                        isDrawnCorrect = isValidated,
                        isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
                        isIntrudersCatcherMode = appLockerPreferences.getIntrudersCatcherEnabled(),
                        isFingerPrintMode = appLockerPreferences.getFingerPrintEnabled()
                    )
            }

        patternValidationViewStateLiveData.addSource(fingerPrintLiveData) {
            patternValidationViewStateLiveData.value = OverlayViewState(
                overlayValidateType = OverlayValidateType.TYPE_FINGERPRINT,
                fingerPrintResultData = it,
                isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
                isIntrudersCatcherMode = appLockerPreferences.getIntrudersCatcherEnabled(),
                isFingerPrintMode = appLockerPreferences.getFingerPrintEnabled()
            )
        }

        val selectedBackgroundId = appLockerPreferences.getSelectedBackgroundId()
        GradientBackgroundDataProvider.gradientViewStateList.forEach {
            if (it.id == selectedBackgroundId) {
                selectedBackgroundDrawableLiveData.value = it
            }
        }
    }

    fun getViewStateObservable(): LiveData<OverlayViewState> = patternValidationViewStateLiveData

    fun getBackgroundDrawableLiveData(): LiveData<GradientItemViewState> =
        selectedBackgroundDrawableLiveData

    fun onPatternDrawn(pattern: List<PatternDot>) {
        patternDrawnSubject.onNext(pattern)
    }

    fun getIntruderPictureImageFile(): File {
        val fileSuffix = System.currentTimeMillis()
        val fileOperationRequest = FileOperationRequest("IMG_$fileSuffix", FileExtension.JPEG, DirectoryType.EXTERNAL)
        return fileManager.createFile(fileOperationRequest,FileManager.SubFolder.INTRUDERS)
    }

}