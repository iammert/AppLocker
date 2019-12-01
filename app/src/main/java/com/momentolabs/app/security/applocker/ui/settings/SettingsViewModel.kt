package com.momentolabs.app.security.applocker.ui.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.data.AppDataProvider
import com.momentolabs.app.security.applocker.data.AppLockerPreferences
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppsDao
import com.momentolabs.app.security.applocker.ui.RxAwareAndroidViewModel
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.extensions.doOnBackground
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val app: Application,
    val appDataProvider: AppDataProvider,
    val lockedAppsDao: LockedAppsDao,
    val appLockerPreferences: AppLockerPreferences
) : RxAwareAndroidViewModel(app) {

    private val settingsViewStateLiveData = MutableLiveData<SettingsViewState>()
        .apply {
            value = SettingsViewState(
                isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
                isFingerPrintEnabled = appLockerPreferences.getFingerPrintEnabled()
            )
        }

    private val fingerPrintStatusViewStateLiveData = MutableLiveData<FingerPrintStatusViewState>()
        .apply {
            with(FingerprintIdentify(app)) {
                init()
                value = FingerPrintStatusViewState(
                    isFingerPrintSupported = isHardwareEnable,
                    isFingerPrintRegistered = isRegisteredFingerprint
                )
            }
        }

    init {
        val installedAppsObservable = appDataProvider.fetchInstalledAppList().toObservable()
        val lockedAppsObservable = lockedAppsDao.getLockedApps().toObservable()

        disposables += IsAllAppsLockedStateCreator.create(
            installedAppsObservable,
            lockedAppsObservable
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isAllAppsLocked ->
                settingsViewStateLiveData.value =
                    SettingsViewState(
                        isAllAppLocked = isAllAppsLocked,
                        isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
                        isFingerPrintEnabled = appLockerPreferences.getFingerPrintEnabled(),
                        isIntrudersCatcherEnabled = appLockerPreferences.getIntrudersCatcherEnabled()
                    )
            }
    }

    fun getSettingsViewStateLiveData(): LiveData<SettingsViewState> = settingsViewStateLiveData

    fun getFingerPrintStatusViewStateLiveData(): LiveData<FingerPrintStatusViewState> =
        fingerPrintStatusViewStateLiveData

    fun isAllLocked() = settingsViewStateLiveData.value?.isAllAppLocked ?: false

    fun isIntrudersCatcherEnabled() = settingsViewStateLiveData.value?.isIntrudersCatcherEnabled ?: false

    fun lockAll() {
        disposables += appDataProvider
            .fetchInstalledAppList()
            .map {
                val entityList: ArrayList<LockedAppEntity> = arrayListOf()
                it.forEach {
                    entityList.add(it.toEntity())
                }
                lockedAppsDao.lockApps(entityList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun unlockAll() {
        disposables += doOnBackground {
            lockedAppsDao.unlockAll()
        }
    }

    fun setHiddenDrawingMode(hiddenDrawingMode: Boolean) {
        appLockerPreferences.setHiddenDrawingMode(hiddenDrawingMode)
        val currentViewState = settingsViewStateLiveData.value
        val updatedViewState = SettingsViewState(
            isAllAppLocked = currentViewState?.isAllAppLocked ?: false,
            isHiddenDrawingMode = hiddenDrawingMode,
            isFingerPrintEnabled = appLockerPreferences.getFingerPrintEnabled(),
            isIntrudersCatcherEnabled = appLockerPreferences.getIntrudersCatcherEnabled()
        )
        settingsViewStateLiveData.value = updatedViewState
    }

    fun setEnableFingerPrint(fingerPrintEnabled: Boolean) {
        appLockerPreferences.setFingerPrintEnable(fingerPrintEnabled)
        val currentViewState = settingsViewStateLiveData.value
        val updatedViewState = SettingsViewState(
            isAllAppLocked = currentViewState?.isAllAppLocked ?: false,
            isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
            isFingerPrintEnabled = fingerPrintEnabled,
            isIntrudersCatcherEnabled = appLockerPreferences.getIntrudersCatcherEnabled()
        )
        settingsViewStateLiveData.value = updatedViewState
    }

    fun setEnableIntrudersCatchers(intruderCatcherEnabled: Boolean) {
        appLockerPreferences.setIntrudersCatcherEnable(intruderCatcherEnabled)
        val currentViewState = settingsViewStateLiveData.value
        val updatedViewState = SettingsViewState(
            isAllAppLocked = currentViewState?.isAllAppLocked ?: false,
            isHiddenDrawingMode = appLockerPreferences.getHiddenDrawingMode(),
            isFingerPrintEnabled = appLockerPreferences.getFingerPrintEnabled(),
            isIntrudersCatcherEnabled = appLockerPreferences.getIntrudersCatcherEnabled()
        )
        settingsViewStateLiveData.value = updatedViewState
    }
}