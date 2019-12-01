package com.momentolabs.app.security.applocker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

open class RxAwareAndroidViewModel(application: Application) : AndroidViewModel(application) {

    var disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}