package com.momentolabs.app.security.applocker.ui.intruders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.momentolabs.app.security.applocker.R

data class IntrudersViewState(val intruderPhotoItemViewStateList: List<IntruderPhotoItemViewState>) {

    fun getEmptyPageVisibility(): Int {
        return if (intruderPhotoItemViewStateList.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    fun getEmptyPageDrawable(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.ic_icon_empty_page_intruders)
    }

    fun getEmptyPageTitle(context: Context): String {
        return context.getString(R.string.empty_page_intruders_title)
    }

    fun getEmptyPageDescription(context: Context): String {
        return context.getString(R.string.empty_page_intruders_description)
    }
}