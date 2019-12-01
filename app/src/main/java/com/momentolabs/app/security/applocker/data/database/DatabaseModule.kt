package com.momentolabs.app.security.applocker.data.database

import android.content.Context
import androidx.room.Room
import com.momentolabs.app.security.applocker.data.database.AppLockerDatabase.Companion.MIGRATION_1_2
import com.momentolabs.app.security.applocker.data.database.AppLockerDatabase.Companion.MIGRATION_2_3
import com.momentolabs.app.security.applocker.data.database.AppLockerDatabase.Companion.MIGRATION_3_4
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    companion object {
        const val DB_NAME = "applocker.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room
            .databaseBuilder(context, AppLockerDatabase::class.java, DB_NAME)
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()

    @Provides
    @Singleton
    fun provideLockedAppsDao(db: AppLockerDatabase) = db.getLockedAppsDao()

    @Provides
    @Singleton
    fun providePatternDao(db: AppLockerDatabase) = db.getPatternDao()

    @Provides
    @Singleton
    fun provideBookmarkDao(db: AppLockerDatabase) = db.getBookmarkDao()

    @Provides
    @Singleton
    fun provideVaultMediaDao(db: AppLockerDatabase) = db.getVaultMediaDao()

    @Provides
    @Singleton
    fun provideBlackListDao(db: AppLockerDatabase) = db.getBlackListDao()

    @Provides
    @Singleton
    fun provideCallLogDao(db: AppLockerDatabase) = db.getCallLogDao()

}