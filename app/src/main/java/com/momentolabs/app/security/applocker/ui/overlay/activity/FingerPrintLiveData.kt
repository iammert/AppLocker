package com.momentolabs.app.security.applocker.ui.overlay.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.bugsnag.android.Bugsnag
import com.crashlytics.android.Crashlytics
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint

class FingerPrintLiveData(context: Context) :
    MutableLiveData<FingerPrintResultData>(),
    BaseFingerprint.ExceptionListener {

    private val fingerprintIdentify: FingerprintIdentify = FingerprintIdentify(context)

    init {
        try {
            fingerprintIdentify.setSupportAndroidL(true)
            fingerprintIdentify.init()
        } catch (exception: Exception) {
            Bugsnag.notify(exception)
        }
    }

    override fun onActive() {
        super.onActive()
        fingerprintIdentify.startIdentify(3, object : BaseFingerprint.IdentifyListener {
            override fun onSucceed() {
                value = FingerPrintResultData.matched()
            }

            override fun onNotMatch(availableTimes: Int) {
                value = FingerPrintResultData.notMatched(availableTimes)
            }

            override fun onFailed(isDeviceLocked: Boolean) {
                value = FingerPrintResultData.error("Fingerprint error")
            }

            override fun onStartFailedByDeviceLocked() {
                value = FingerPrintResultData.error("Fingerprint error")
            }
        })
    }

    override fun onInactive() {
        super.onInactive()
        fingerprintIdentify.cancelIdentify()
    }

    override fun onCatchException(exception: Throwable?) {
        Crashlytics.logException(exception)
        value = FingerPrintResultData.error(exception?.message ?: "")
    }

}