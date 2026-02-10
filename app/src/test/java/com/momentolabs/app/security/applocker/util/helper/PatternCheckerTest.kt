package com.momentolabs.app.security.applocker.util.helper

import com.momentolabs.app.security.applocker.data.database.pattern.PatternDot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PatternCheckerTest {

    private val pattern = listOf(PatternDot(0, 0), PatternDot(0, 1), PatternDot(0, 2))

    @Test
    fun equalPatternsMatch() {
        assertTrue(PatternChecker.checkPatternsEqual(pattern, pattern.map { it.copy() }))
    }

    @Test
    fun orderMatters() {
        assertFalse(PatternChecker.checkPatternsEqual(pattern, pattern.reversed()))
    }

    @Test
    fun emptyPatternsNeverMatch() {
        assertFalse(PatternChecker.checkPatternsEqual(emptyList(), emptyList()))
    }

    @Test
    fun differentLengthsDoNotMatch() {
        assertFalse(PatternChecker.checkPatternsEqual(pattern, pattern.dropLast(1)))
    }
}
