package com.momentolabs.app.security.applocker.data.database.callblocker.blacklist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class BlackListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addToBlacklist(blackListItemEntity: BlackListItemEntity)

    @Query("SELECT * FROM blacklist")
    abstract fun getBlackList(): Flowable<List<BlackListItemEntity>>

    @Query("DELETE FROM blacklist WHERE blacklist_id = :blackListId")
    abstract fun delete(blackListId: Int)

}