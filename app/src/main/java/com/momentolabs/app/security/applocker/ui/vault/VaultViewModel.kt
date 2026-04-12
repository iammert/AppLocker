package com.momentolabs.app.security.applocker.ui.vault

import com.momentolabs.app.security.applocker.repository.UserPreferencesRepository
import com.momentolabs.app.security.applocker.repository.VaultRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.helper.ErrorLogger
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VaultViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository,
    private val vaultRepository: VaultRepository
) : RxAwareViewModel() {

    fun shouldShowRateUs(): Boolean {
        return userPreferencesRepository.isUserRateUs().not()
    }

    fun setRateUsAsked() {
        userPreferencesRepository.setRateUsAsked()
    }

    override fun onCleared() {
        // Not added to the view model's disposables, those are disposed right after this call.
        vaultRepository.clearDecryptedPreviews()
            .subscribeOn(Schedulers.io())
            .subscribe({}, ErrorLogger::log)
        super.onCleared()
    }
}
