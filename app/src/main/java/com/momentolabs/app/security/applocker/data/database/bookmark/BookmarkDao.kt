package com.momentolabs.app.security.applocker.data.database.bookmark

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable

@Dao
abstract class BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addToBookmark(bookmarkEntity: BookmarkEntity)

    @Query("SELECT * FROM bookmark")
    abstract fun getBookmarks(): Flowable<List<BookmarkEntity>>

    @Query("DELETE FROM bookmark WHERE url = :url")
    abstract fun deleteFromBookmark(url: String)

    @Query("SELECT COUNT(*) FROM bookmark WHERE url = :url")
    abstract fun isBookmarkExist(url: String): Int
}