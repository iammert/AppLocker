package com.momentolabs.app.security.applocker.ui.browser.bookmarks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkEntity
import com.momentolabs.app.security.applocker.databinding.DialogBookmarksBinding
import com.momentolabs.app.security.applocker.ui.BaseBottomSheetDialog
import com.momentolabs.app.security.applocker.util.delegate.inflate

class BookmarksDialog : BaseBottomSheetDialog<BookmarksViewModel>() {

    interface BookmarkItemSelectListener {
        fun onBookmarkItemSelected(bookmarkEntity: BookmarkEntity)
    }

    private val binding: DialogBookmarksBinding by inflate(R.layout.dialog_bookmarks)

    private val bookmarksAdapter = BookmarksAdapter()

    private var bookmarkItemSelectListener: BookmarkItemSelectListener? = null

    override fun getViewModel(): Class<BookmarksViewModel> = BookmarksViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.imageViewBack.setOnClickListener { dismiss() }
        binding.recyclerViewBookmarks.adapter = bookmarksAdapter
        bookmarksAdapter.itemClickListener = {
            bookmarkItemSelectListener?.onBookmarkItemSelected(it)
            dismiss()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getBookmarksLiveData().observe(this, Observer { bookmarksAdapter.updateBookmarks(it) })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BookmarkItemSelectListener) {
            this.bookmarkItemSelectListener = context
        }
    }

    companion object {
        fun newInstance(): AppCompatDialogFragment {
            val dialog = BookmarksDialog()
            return dialog
        }
    }

}