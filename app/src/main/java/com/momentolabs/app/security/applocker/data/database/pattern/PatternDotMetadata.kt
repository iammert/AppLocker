package com.momentolabs.app.security.applocker.data.database.pattern

/** Format of the plain pattern stored before database version 5. Only used by the migration. */
data class PatternDotMetadata(val pattern: List<PatternDot>)
