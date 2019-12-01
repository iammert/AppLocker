package com.momentolabs.app.security.applocker.util.extensions

import com.andrognito.patternlockview.PatternLockView
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDot

fun List<PatternLockView.Dot>.convertToPatternDot(): List<PatternDot> {
    val patternDotList: ArrayList<PatternDot> = arrayListOf()
    forEach {
        patternDotList.add(PatternDot(column = it.column, row = it.row))
    }
    return patternDotList
}