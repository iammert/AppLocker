package com.momentolabs.app.security.applocker.ui.callblocker.log

import android.view.View

data class CallLogViewState(val callLogsViewState: List<CallLogItemViewState>) {

    fun getEmptyPageVisibility(): Int = if (callLogsViewState.isEmpty()) View.VISIBLE else View.GONE

    companion object {

        fun empty() = CallLogViewState(arrayListOf())

    }
}
