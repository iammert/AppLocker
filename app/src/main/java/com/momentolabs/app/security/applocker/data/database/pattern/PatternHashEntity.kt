package com.momentolabs.app.security.applocker.data.database.pattern

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The lock pattern, stored as a salted PBKDF2 hash. See [PatternHasher].
 */
@Entity(tableName = "pattern_hash")
data class PatternHashEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = SINGLE_PATTERN_ID,
    @ColumnInfo(name = "salt")
    val salt: String,
    @ColumnInfo(name = "hash")
    val hash: String
) {
    companion object {
        const val SINGLE_PATTERN_ID = 0
    }
}
