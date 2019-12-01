package com.momentolabs.app.security.applocker.util.helper

import com.momentolabs.app.security.applocker.data.database.pattern.PatternDot

object PatternChecker {

    fun checkPatternsEqual(pattern1: List<PatternDot>, pattern2: List<PatternDot>): Boolean {
        if (pattern1.isEmpty() || pattern2.isEmpty()) {
            return false
        }

        if (pattern1.size != pattern2.size) {
            return false
        }

        var checkBothSame = true
        for (i in 0 until pattern1.size) {
            if (pattern1[i].row != pattern2[i].row || pattern1[i].column != pattern2[i].column) {
                checkBothSame = false
            }
        }
        return checkBothSame
    }
}