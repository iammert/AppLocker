package com.momentolabs.app.security.applocker.ui.browser.bookmarks

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkEntity
import com.momentolabs.app.security.applocker.databinding.ItemBookmarkBinding
import com.momentolabs.app.security.applocker.util.extensions.inflateAdapterItem

class BookmarksAdapter : RecyclerView.Adapter<BookmarksAdapter.BookmarkItemViewHolder>() {

    var itemClickListener: ((BookmarkEntity) -> Unit)? = null

    private val bookmarkItemList = arrayListOf<BookmarkEntity>()

    fun updateBookmarks(bookmarkItemList: List<BookmarkEntity>) {
        this.bookmarkItemList.clear()
        this.bookmarkItemList.addAll(bookmarkItemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkItemViewHolder =
        BookmarkItemViewHolder.create(parent, itemClickListener)

    override fun getItemCount(): Int = bookmarkItemList.size

    override fun onBindViewHolder(holder: BookmarkItemViewHolder, position: Int) =
        holder.bind(bookmarkItemList[position])

    class BookmarkItemViewHolder(val binding: ItemBookmarkBinding, private val itemClickListener: ((BookmarkEntity) -> Unit)?) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { itemClickListener?.invoke(binding.viewState!!.bookmarkEntity) }
        }

        fun bind(bookmarkEntity: BookmarkEntity) {
            binding.viewState = BookmarkItemViewState(bookmarkEntity)
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup, itemClickListener: ((BookmarkEntity) -> Unit)?): BookmarkItemViewHolder {
                val binding: ItemBookmarkBinding = parent.inflateAdapterItem(R.layout.item_bookmark)
                return BookmarkItemViewHolder(binding, itemClickListener)
            }
        }

    }
}