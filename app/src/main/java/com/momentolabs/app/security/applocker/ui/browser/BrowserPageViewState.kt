package com.momentolabs.app.security.applocker.ui.browser

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.R

data class BrowserPageViewState(
    val url: String = "",
    val webPageState: WebPageState = WebPageState.NONE,
    var loadingProgress: Int = 0,
    var isBookmarked: Boolean
) {

    fun getBookmarkIcon(context: Context): Drawable? =
        if (isBookmarked) {
            ContextCompat.getDrawable(context, R.drawable.ic_round_bookmark_24px)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_round_bookmark_border_24px)
        }

    fun loadingVisibility(): Int = if (webPageState == WebPageState.LOADING) View.VISIBLE else View.INVISIBLE

    fun quickBrowsingVisibility(): Int = if (webPageState == WebPageState.NONE) View.VISIBLE else View.GONE

    fun updateLoading(progress: Int): BrowserPageViewState =
        copy(loadingProgress = progress, webPageState = WebPageState.LOADING)

    fun getLoadingProgressPercent(): Int = loadingProgress

    fun complete(): BrowserPageViewState = copy(webPageState = WebPageState.LOADED, loadingProgress = 100)

    companion object {

        fun empty() = BrowserPageViewState("", WebPageState.NONE, 0, false)

        fun load(url: String, isBookmarked: Boolean) = BrowserPageViewState(url, WebPageState.REQUEST, 0, isBookmarked)
    }

}