package com.momentolabs.app.security.applocker.repository

import com.momentolabs.app.security.applocker.data.AppLockerPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(val appLockerPreferences: AppLockerPreferences) {

    private var isRateUsAskedInThisSession = false

    fun setRateUsAsked() {
        isRateUsAskedInThisSession = true
    }

    fun setUserRateUs() {
        appLockerPreferences.setUserRateUs()
    }

    fun isUserRateUs(): Boolean {
        return isRateUsAskedInThisSession || appLockerPreferences.isUserRateUs()
    }

    fun isPrivacyPolicyAccepted(): Boolean {
        return appLockerPreferences.isPrivacyPolicyAccepted()
    }

    fun endSession(){
        isRateUsAskedInThisSession = false
    }
}