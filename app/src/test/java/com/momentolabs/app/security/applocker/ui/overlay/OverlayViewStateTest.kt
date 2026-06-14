package com.momentolabs.app.security.applocker.ui.overlay

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.momentolabs.app.security.applocker.R
import com.momentolabs.app.security.applocker.ui.overlay.activity.FingerPrintResultData
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverlayViewStateTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun wrongPatternShowsWrongPatternPrompt() {
        val state = OverlayViewState(overlayValidateType = OverlayValidateType.TYPE_PATTERN, isDrawnCorrect = false)

        assertEquals(context.getString(R.string.overlay_prompt_pattern_title_wrong), state.getPromptMessage(context))
    }

    @Test
    fun lockoutOverridesEveryOtherPrompt() {
        val state = OverlayViewState(
            overlayValidateType = OverlayValidateType.TYPE_PATTERN,
            isDrawnCorrect = false,
            lockoutSeconds = 30
        )

        assertEquals(
            context.resources.getQuantityString(R.plurals.overlay_prompt_locked_out, 30, 30),
            state.getPromptMessage(context)
        )
    }

    @Test
    fun fingerprintErrorAsksForThePattern() {
        val state = OverlayViewState(
            overlayValidateType = OverlayValidateType.TYPE_FINGERPRINT,
            fingerPrintResultData = FingerPrintResultData.error("Lockout")
        )

        assertEquals(context.getString(R.string.overlay_prompt_fingerprint_title_error), state.getPromptMessage(context))
    }
}
