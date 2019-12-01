package com.momentolabs.app.security.applocker.ui.rateus

import com.momentolabs.app.security.applocker.repository.UserPreferencesRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import javax.inject.Inject

class RateUsViewModel @Inject constructor(val userPreferencesRepository: UserPreferencesRepository) :
    RxAwareViewModel() {

    fun setUserRateUs() = userPreferencesRepository.setUserRateUs()
}