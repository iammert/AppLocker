package com.momentolabs.app.security.applocker.ui.overlay.activity

import com.momentolabs.app.security.applocker.ui.overlay.activity.FingerPrintResult.*

data class FingerPrintResultData(
    val fingerPrintResult: FingerPrintResult,
    val availableTimes: Int = 0,
    val errorMessage: String = ""
) {

    fun isSucces() = fingerPrintResult == SUCCESS

    fun isNotSucces() = fingerPrintResult != SUCCESS

    companion object {

        fun matched() = FingerPrintResultData(SUCCESS)

        fun notMatched(availableTimes: Int) = FingerPrintResultData(
            fingerPrintResult = NOT_MATCHED,
            availableTimes = availableTimes
        )

        fun error(errorMessage: String) =
            FingerPrintResultData(
                fingerPrintResult = ERROR,
                errorMessage = errorMessage
            )
    }
}