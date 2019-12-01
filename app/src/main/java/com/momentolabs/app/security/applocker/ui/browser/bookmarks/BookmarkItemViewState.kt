package com.momentolabs.app.security.applocker.ui.browser.bookmarks

import android.view.View
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkEntity
import java.net.URL

data class BookmarkItemViewState(val bookmarkEntity: BookmarkEntity) {

    fun getTitleText(): String {
        return if (bookmarkEntity.description.isNotEmpty()) {
            bookmarkEntity.description
        } else if (bookmarkEntity.title.isNotEmpty()) {
            bookmarkEntity.title
        } else {
            URL(bookmarkEntity.url).host
        }
    }

    fun getSubtitleText(): String {
        return URL(bookmarkEntity.url).host
    }

    fun getImageUrl() = bookmarkEntity.imageUrl

    fun getImageViewVisibility(): Int = if (bookmarkEntity.imageUrl.isNotEmpty()) View.VISIBLE else View.GONE
}