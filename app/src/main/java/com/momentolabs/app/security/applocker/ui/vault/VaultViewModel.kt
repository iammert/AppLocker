package com.momentolabs.app.security.applocker.ui.vault

import com.momentolabs.app.security.applocker.repository.UserPreferencesRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import javax.inject.Inject

class VaultViewModel @Inject constructor(val userPreferencesRepository: UserPreferencesRepository) : RxAwareViewModel() {

    fun shouldShowRateUs(): Boolean {
        return userPreferencesRepository.isUserRateUs().not()
    }

    fun setRateUsAsked() {
        userPreferencesRepository.setRateUsAsked()
    }
}