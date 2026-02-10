package com.momentolabs.app.security.applocker.data.database.pattern

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PatternHasherTest {

    private val pattern = listOf(PatternDot(0, 0), PatternDot(1, 0), PatternDot(2, 0), PatternDot(2, 1))

    @Test
    fun matchingPatternIsAccepted() {
        assertTrue(PatternHasher.matches(pattern, PatternHasher.hash(pattern)))
    }

    @Test
    fun differentPatternIsRejected() {
        val stored = PatternHasher.hash(pattern)

        assertFalse(PatternHasher.matches(pattern.reversed(), stored))
        assertFalse(PatternHasher.matches(pattern.dropLast(1), stored))
        assertFalse(PatternHasher.matches(emptyList(), stored))
    }

    @Test
    fun sameSaltIsNeverReused() {
        val first = PatternHasher.hash(pattern)
        val second = PatternHasher.hash(pattern)

        assertNotEquals(first.salt, second.salt)
        assertNotEquals(first.hash, second.hash)
    }

    @Test
    fun patternIsNotStoredInPlainText() {
        val stored = PatternHasher.hash(pattern)

        assertFalse(stored.hash.contains("0123"))
        assertFalse(stored.hash.contains("row"))
    }
}
