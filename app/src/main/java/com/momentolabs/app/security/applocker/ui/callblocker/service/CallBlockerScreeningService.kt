package com.momentolabs.app.security.applocker.ui.callblocker.service

import android.annotation.TargetApi
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import com.bugsnag.android.Bugsnag
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.momentolabs.app.security.applocker.data.database.callblocker.blacklist.BlackListItemEntity
import com.momentolabs.app.security.applocker.data.database.callblocker.calllog.CallLogItemEntity
import com.momentolabs.app.security.applocker.repository.CallBlockerRepository
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

@TargetApi(Build.VERSION_CODES.N)
class CallBlockerScreeningService : CallScreeningService() {

    @Inject
    lateinit var callBlockerRepository: CallBlockerRepository

    private var blackListDisposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onScreenCall(details: Call.Details) {

        if (details.handle == null || details.handle.schemeSpecificPart == null) {
            releaseCall(details = details)
            return
        }

        blackListDisposable?.let {
            if (it.isDisposed.not()) {
                it.dispose()
            }
        }

        blackListDisposable = callBlockerRepository.getBlackList()
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { blackList ->
                    var isInBlackList = false
                    var itemEntity: BlackListItemEntity? = null
                    blackList
                        .takeWhile { isInBlackList.not() }
                        .forEach {
                            val incomingPhoneNumber = details.handle.schemeSpecificPart
                            val matchType =
                                PhoneNumberUtil.getInstance().isNumberMatch(it.phoneNumber, incomingPhoneNumber)
                            if (isPhoneMatch(matchType)) {
                                isInBlackList = true
                                itemEntity = it
                            }
                        }


                    if (isInBlackList) {
                        rejectCall(details = details)
                        itemEntity?.let { saveCallLog(it) }

                    } else {
                        releaseCall(details = details)
                    }
                },
                { error -> Bugsnag.notify(error) })
    }

    private fun isPhoneMatch(matchResult: PhoneNumberUtil.MatchType): Boolean {
        return matchResult == PhoneNumberUtil.MatchType.EXACT_MATCH ||
                matchResult == PhoneNumberUtil.MatchType.NSN_MATCH ||
                matchResult == PhoneNumberUtil.MatchType.SHORT_NSN_MATCH
    }

    private fun saveCallLog(blackListItemEntity: BlackListItemEntity) {
        CallLogItemEntity(
            logDate = Date(),
            userName = blackListItemEntity.userName,
            phoneNumber = blackListItemEntity.phoneNumber
        ).also {
            callBlockerRepository.addToCallLog(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    private fun rejectCall(details: Call.Details) {
        respondToCall(
            details,
            CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipNotification(true)
                .setSkipCallLog(true)
                .build()
        )
    }

    private fun releaseCall(details: Call.Details) {
        respondToCall(details, CallResponse.Builder().build())
    }
}