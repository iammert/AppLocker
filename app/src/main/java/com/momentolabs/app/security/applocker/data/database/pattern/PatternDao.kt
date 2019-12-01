package com.momentolabs.app.security.applocker.data.database.pattern

import androidx.room.*
import io.reactivex.Flowable

@Dao
abstract class PatternDao {

    @Transaction
    open fun createPattern(patternEntity: PatternEntity) {
        deletePattern()
        insertPattern(patternEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPattern(patternEntity: PatternEntity)

    @Query("SELECT * FROM pattern LIMIT 1")
    abstract fun getPattern(): Flowable<PatternEntity>

    @Query("SELECT count(*) FROM pattern")
    abstract fun isPatternCreated(): Flowable<Int>

    @Query("DELETE FROM pattern")
    abstract fun deletePattern()
}