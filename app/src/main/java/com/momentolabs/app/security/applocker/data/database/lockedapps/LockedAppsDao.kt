package com.momentolabs.app.security.applocker.data.database.lockedapps

import androidx.room.*
import io.reactivex.Flowable

@Dao
abstract class LockedAppsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun lockApp(lockedAppEntity: LockedAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun lockApps(lockedAppEntityList: List<LockedAppEntity>)

    @Query("SELECT * FROM locked_app")
    abstract fun getLockedApps(): Flowable<List<LockedAppEntity>>

    @Query("SELECT * FROM locked_app")
    abstract fun getLockedAppsSync(): List<LockedAppEntity>

    @Query("DELETE FROM locked_app WHERE packageName = :packageName")
    abstract fun unlockApp(packageName: String)

    @Query("DELETE FROM locked_app")
    abstract fun unlockAll()
}