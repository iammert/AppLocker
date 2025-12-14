package com.momentolabs.app.security.applocker.ui.overlay.activity

import com.momentolabs.app.security.applocker.ui.overlay.activity.FingerPrintResult.*

data class FingerPrintResultData(
    val fingerPrintResult: FingerPrintResult,
    val errorMessage: String = ""
) {

    fun isSucces() = fingerPrintResult == SUCCESS

    fun isNotSucces() = fingerPrintResult != SUCCESS

    companion object {

        fun matched() = FingerPrintResultData(SUCCESS)

        fun notMatched() = FingerPrintResultData(fingerPrintResult = NOT_MATCHED)

        fun error(errorMessage: String) =
            FingerPrintResultData(
                fingerPrintResult = ERROR,
                errorMessage = errorMessage
            )
    }
}