package com.momentolabs.app.security.applocker.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkDao
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkEntity
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.ui.browser.resolver.UrlResolver
import com.momentolabs.app.security.applocker.util.extensions.doOnBackground
import com.momentolabs.app.security.applocker.util.extensions.plusAssign
import com.raqun.beaverlib.Beaver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BrowserViewModel @Inject constructor(val bookmarkDao: BookmarkDao) : RxAwareViewModel() {

    private val browserViewStateLiveData = MutableLiveData<BrowserPageViewState>()
        .apply {
            value = BrowserPageViewState.empty()
        }

    fun getBrowserViewStateLiveData(): LiveData<BrowserPageViewState> = browserViewStateLiveData

    fun load(url: String) {
        disposables += Observable
            .create<Boolean> {
                val count = bookmarkDao.isBookmarkExist(url)
                it.onNext(count > 0)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                browserViewStateLiveData.value =
                    BrowserPageViewState.load(url = UrlResolver.resolveUrl(url), isBookmarked = it)
            }
    }

    fun updateLoadingProgress(progress: Int) {
        val currentValue = browserViewStateLiveData.value
        currentValue?.let {
            browserViewStateLiveData.value = it.updateLoading(progress)
        }
    }

    fun complete() {
        val currentValue = browserViewStateLiveData.value
        currentValue?.let {
            browserViewStateLiveData.value = it.complete()
        }
    }

    fun isResetMode(): Boolean {
        val currentValue = browserViewStateLiveData.value
        return currentValue?.webPageState == WebPageState.NONE
    }

    fun completeWithUrl(url: String) {
        disposables += Observable
            .create<Boolean> {
                val count = bookmarkDao.isBookmarkExist(url)
                it.onNext(count > 0)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isBookmarked ->
                val currentValue = browserViewStateLiveData.value
                currentValue?.let {
                    browserViewStateLiveData.value =
                        it.copy(url = url, webPageState = WebPageState.LOADED, isBookmarked = isBookmarked)
                }
            }
    }

    fun reset() {
        browserViewStateLiveData.value = BrowserPageViewState.empty()
    }

    fun isCurrentBookmarked(): Boolean {
        return browserViewStateLiveData.value?.isBookmarked ?: false
    }

    fun addToBookmarks() {
        val url = browserViewStateLiveData.value?.url

        if (url.isNullOrEmpty()) {
            return
        }

        GlobalScope.launch {
            val urlMetadata = Beaver.load(url).await()
            val bookmark = BookmarkEntity(
                url = url,
                name = urlMetadata?.name ?: "",
                title = urlMetadata?.title ?: "",
                imageUrl = urlMetadata?.imageUrl ?: "",
                description = urlMetadata?.desc ?: "",
                mediaType = urlMetadata?.mediaType ?: ""
            )
            doOnBackground { bookmarkDao.addToBookmark(bookmark) }
        }

        val currentValue = browserViewStateLiveData.value
        currentValue?.let {
            browserViewStateLiveData.value = it.copy(isBookmarked = true)
        }
    }

    fun removeFromBookmark() {
        val url = browserViewStateLiveData.value?.url

        if (url.isNullOrEmpty()) {
            return
        }

        doOnBackground { bookmarkDao.deleteFromBookmark(url) }
        val currentValue = browserViewStateLiveData.value
        currentValue?.let {
            browserViewStateLiveData.value = it.copy(isBookmarked = false)
        }
    }
}