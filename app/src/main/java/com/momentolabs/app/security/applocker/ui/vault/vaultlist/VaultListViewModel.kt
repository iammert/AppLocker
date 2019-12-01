package com.momentolabs.app.security.applocker.ui.vault.vaultlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaDao
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaType.*
import com.momentolabs.app.security.applocker.repository.VaultRepository
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VaultListViewModel @Inject constructor(private val vaultMediaRepository: VaultRepository) : RxAwareViewModel() {

    private val vaultListViewStateLiveData = MutableLiveData<VaultListViewState>()

    private lateinit var vaultMediaType: VaultMediaType

    fun setVaultMediaType(vaultMediaType: VaultMediaType) {
        this.vaultMediaType = vaultMediaType
        vaultListViewStateLiveData.value = VaultListViewState.empty(vaultMediaType)

        val vaultMediaFlowable = when (vaultMediaType) {
            TYPE_IMAGE -> vaultMediaRepository.getVaultImages()
            TYPE_VIDEO -> vaultMediaRepository.getVaultVideos()
        }

        disposables += vaultMediaFlowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                vaultListViewStateLiveData.value = VaultListViewState(
                    vaultMediaType = vaultMediaType,
                    vaultList = it
                )
            }
    }

    fun getVaultListViewStateLiveData(): LiveData<VaultListViewState> = vaultListViewStateLiveData


}