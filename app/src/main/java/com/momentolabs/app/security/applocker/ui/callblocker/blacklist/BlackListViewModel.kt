package com.momentolabs.app.security.applocker.ui.callblocker.blacklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.repository.CallBlockerRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BlackListViewModel @Inject constructor(val callBlockerRepository: CallBlockerRepository) : RxAwareViewModel() {

    private val blackListViewStateLiveData = MutableLiveData<BlackListViewState>()

    init {
        blackListViewStateLiveData.value = BlackListViewState.empty()

        disposables += callBlockerRepository.getBlackList()
            .map {
                val itemViewStateList = arrayListOf<BlackListItemViewState>()
                it.forEach { itemViewStateList.add(BlackListItemViewState(it)) }
                BlackListViewState(itemViewStateList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                blackListViewStateLiveData.value = it
            }
    }

    fun getViewStateLiveData(): LiveData<BlackListViewState> = blackListViewStateLiveData

}