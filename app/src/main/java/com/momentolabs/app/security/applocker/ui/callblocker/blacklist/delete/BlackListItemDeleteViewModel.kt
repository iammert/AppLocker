package com.momentolabs.app.security.applocker.ui.callblocker.blacklist.delete

import com.momentolabs.app.security.applocker.repository.CallBlockerRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BlackListItemDeleteViewModel @Inject constructor(val callBlockerRepository: CallBlockerRepository) :
    RxAwareViewModel() {

    fun deleteFromBlackList(blackListItemId: Int) {
        disposables += callBlockerRepository.deleteBlackListItem(blackListItemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}