package com.momentolabs.app.security.applocker.data.database.pattern

import androidx.room.TypeConverter
import com.andrognito.patternlockview.PatternLockView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PatternTypeConverter {

    @TypeConverter
    fun patternToString(patternMetadata: PatternDotMetadata): String {
        return Gson().toJson(patternMetadata)
    }

    @TypeConverter
    fun stringToPattern(patternJson: String): PatternDotMetadata {
        return Gson().fromJson(patternJson, PatternDotMetadata::class.java)
    }
}