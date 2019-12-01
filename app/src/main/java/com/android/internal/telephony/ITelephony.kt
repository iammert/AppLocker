package com.android.internal.telephony

interface ITelephony{
    fun endCall(): Boolean

    fun answerRingingCall()

    fun silenceRinger()
}