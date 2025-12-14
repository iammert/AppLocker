package com.momentolabs.app.security.applocker.ui.overlay.activity

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.momentolabs.app.security.applocker.R

/**
 * Asks for the fingerprint with the system [BiometricPrompt] and reports the result as
 * [FingerPrintResultData]. Cancelling the prompt reports nothing, so the pattern can be used.
 */
class BiometricAuthenticator(
    private val activity: FragmentActivity,
    private val onResult: (FingerPrintResultData) -> Unit
) {

    private val prompt = BiometricPrompt(
        activity,
        ContextCompat.getMainExecutor(activity),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onResult(FingerPrintResultData.matched())
            }

            override fun onAuthenticationFailed() {
                onResult(FingerPrintResultData.notMatched())
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode !in IGNORED_ERRORS) {
                    onResult(FingerPrintResultData.error(errString.toString()))
                }
            }
        }
    )

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(activity.getString(R.string.app_name))
        .setNegativeButtonText(activity.getString(R.string.overlay_use_pattern))
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .build()

    fun authenticate() {
        prompt.authenticate(promptInfo)
    }

    fun cancel() {
        prompt.cancelAuthentication()
    }

    companion object {

        private val IGNORED_ERRORS = setOf(
            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
            BiometricPrompt.ERROR_USER_CANCELED,
            BiometricPrompt.ERROR_CANCELED
        )

        fun canAuthenticate(context: Context): Int =
            BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)
    }
}
