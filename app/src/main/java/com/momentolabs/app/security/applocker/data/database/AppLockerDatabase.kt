package com.momentolabs.app.security.applocker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkDao
import com.momentolabs.app.security.applocker.data.database.bookmark.BookmarkEntity
import com.momentolabs.app.security.applocker.data.database.callblocker.blacklist.BlackListDao
import com.momentolabs.app.security.applocker.data.database.callblocker.blacklist.BlackListItemEntity
import com.momentolabs.app.security.applocker.data.database.callblocker.calllog.CallLogDao
import com.momentolabs.app.security.applocker.data.database.callblocker.calllog.CallLogItemEntity
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppsDao
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDao
import com.momentolabs.app.security.applocker.data.database.pattern.PatternEntity
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaDao
import com.momentolabs.app.security.applocker.data.database.vault.VaultMediaEntity


@Database(
    entities = [LockedAppEntity::class, PatternEntity::class, BookmarkEntity::class, VaultMediaEntity::class, BlackListItemEntity::class, CallLogItemEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateTypeConverters::class)
abstract class AppLockerDatabase : RoomDatabase() {

    abstract fun getLockedAppsDao(): LockedAppsDao

    abstract fun getPatternDao(): PatternDao

    abstract fun getBookmarkDao(): BookmarkDao

    abstract fun getVaultMediaDao(): VaultMediaDao

    abstract fun getBlackListDao(): BlackListDao

    abstract fun getCallLogDao(): CallLogDao

    companion object {

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `bookmark` (`url` TEXT NOT NULL,`name` TEXT NOT NULL,`image_url` TEXT NOT NULL,`title` TEXT NOT NULL,`description` TEXT NOT NULL,`media_type` TEXT NOT NULL, PRIMARY KEY(`url`))")
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `blacklist` (`blacklist_id` INTEGER NOT NULL,`user_name` TEXT NOT NULL,`phone_number` TEXT NOT NULL, PRIMARY KEY(`blacklist_id`))")
                database.execSQL("CREATE TABLE IF NOT EXISTS `call_log` (`log_id` INTEGER NOT NULL,`call_log_time` INTEGER NOT NULL,`user_name` TEXT NOT NULL,`phone_number` TEXT NOT NULL, PRIMARY KEY(`log_id`))")
            }
        }

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS `vault_media`")
                database.execSQL("CREATE TABLE IF NOT EXISTS `vault_media` (`original_path` TEXT NOT NULL,`original_file_name` TEXT NOT NULL,`encrypted_path` TEXT NOT NULL, `encrypted_preview_path` TEXT NOT NULL,`media_type` TEXT NOT NULL, PRIMARY KEY(`original_path`))")
            }
        }
    }
}