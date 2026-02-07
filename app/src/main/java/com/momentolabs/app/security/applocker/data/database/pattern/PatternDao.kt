package com.momentolabs.app.security.applocker.data.database.pattern

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Flowable

@Dao
abstract class PatternDao {

    @Transaction
    open fun createPattern(patternHashEntity: PatternHashEntity) {
        deletePattern()
        insertPattern(patternHashEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPattern(patternHashEntity: PatternHashEntity)

    @Query("SELECT * FROM pattern_hash LIMIT 1")
    abstract fun getPattern(): Flowable<PatternHashEntity>

    @Query("SELECT count(*) FROM pattern_hash")
    abstract fun isPatternCreated(): Flowable<Int>

    @Query("DELETE FROM pattern_hash")
    abstract fun deletePattern()
}
