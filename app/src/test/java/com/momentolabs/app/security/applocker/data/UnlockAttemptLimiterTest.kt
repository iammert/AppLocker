package com.momentolabs.app.security.applocker.data

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import java.time.Duration

@RunWith(AndroidJUnit4::class)
class UnlockAttemptLimiterTest {

    private lateinit var limiter: UnlockAttemptLimiter

    @Before
    fun setUp() {
        limiter = UnlockAttemptLimiter(ApplicationProvider.getApplicationContext())
        limiter.onSuccessfulAttempt()
    }

    @Test
    fun locksOutAfterTooManyFailures() {
        repeat(UnlockAttemptLimiter.MAX_FAILED_ATTEMPTS - 1) {
            assertEquals(0, limiter.onFailedAttempt())
        }

        assertEquals(30, limiter.onFailedAttempt())
        assertEquals(30, limiter.remainingLockoutSeconds())
    }

    @Test
    fun lockoutEndsAfterItsDuration() {
        repeat(UnlockAttemptLimiter.MAX_FAILED_ATTEMPTS) { limiter.onFailedAttempt() }

        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(10))
        assertTrue(limiter.remainingLockoutSeconds() in 19..20)

        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(21))
        assertEquals(0, limiter.remainingLockoutSeconds())
    }

    @Test
    fun successResetsTheFailureCount() {
        repeat(UnlockAttemptLimiter.MAX_FAILED_ATTEMPTS - 1) { limiter.onFailedAttempt() }

        limiter.onSuccessfulAttempt()

        assertEquals(0, limiter.onFailedAttempt())
    }
}
