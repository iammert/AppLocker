package com.momentolabs.app.security.applocker.data.database.callblocker.calllog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class CallLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addToLog(callLogItemEntity: CallLogItemEntity)

    @Query("SELECT * FROM call_log")
    abstract fun getCallLogs(): Flowable<List<CallLogItemEntity>>

    @Query("DELETE FROM call_log WHERE log_id = :logId")
    abstract fun delete(logId: Int)
}