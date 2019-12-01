package com.momentolabs.app.security.applocker.util.encryptor

import java.io.File

sealed class CryptoProcess {

    data class Processing(val percentage: Int) : CryptoProcess()

    data class Complete(val file: File) : CryptoProcess()


    companion object {

        fun processing(percentage: Int): CryptoProcess = Processing(percentage = percentage)

        fun complete(file: File): CryptoProcess = Complete(file)
    }
}