package com.momentolabs.app.security.applocker.ui.callblocker.blacklist

import android.view.View

data class BlackListViewState(val blackListViewStateList: List<BlackListItemViewState>) {

    fun getEmptyPageVisibility(): Int = if (blackListViewStateList.isEmpty()) View.VISIBLE else View.GONE

    companion object {

        fun empty() = BlackListViewState(arrayListOf())
    }
}