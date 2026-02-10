package com.momentolabs.app.security.applocker.data.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDot
import com.momentolabs.app.security.applocker.data.database.pattern.PatternHashEntity
import com.momentolabs.app.security.applocker.data.database.pattern.PatternHasher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private lateinit var helper: SupportSQLiteOpenHelper
    private lateinit var database: SupportSQLiteDatabase

    @Before
    fun setUp() {
        val configuration = SupportSQLiteOpenHelper.Configuration
            .builder(ApplicationProvider.getApplicationContext())
            .name(null)
            .callback(object : SupportSQLiteOpenHelper.Callback(4) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE `pattern` (`pattern_metadata` TEXT NOT NULL, PRIMARY KEY(`pattern_metadata`))")
                }

                override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
            })
            .build()
        helper = FrameworkSQLiteOpenHelperFactory().create(configuration)
        database = helper.writableDatabase
    }

    @After
    fun tearDown() {
        helper.close()
    }

    @Test
    fun migration4To5HashesTheStoredPattern() {
        database.execSQL(
            "INSERT INTO `pattern` VALUES ('{\"pattern\":[{\"column\":0,\"row\":0},{\"column\":1,\"row\":1},{\"column\":2,\"row\":2},{\"column\":2,\"row\":1}]}')"
        )

        AppLockerDatabase.MIGRATION_4_5.migrate(database)

        val stored = database.query("SELECT `id`, `salt`, `hash` FROM `pattern_hash`").use { cursor ->
            assertEquals(1, cursor.count)
            cursor.moveToFirst()
            PatternHashEntity(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
        }
        val pattern = listOf(PatternDot(0, 0), PatternDot(1, 1), PatternDot(2, 2), PatternDot(2, 1))
        assertTrue(PatternHasher.matches(pattern, stored))
        assertFalse(PatternHasher.matches(pattern.reversed(), stored))
        assertFalse(tableExists("pattern"))
    }

    @Test
    fun migration4To5WithoutPatternCreatesEmptyTable() {
        AppLockerDatabase.MIGRATION_4_5.migrate(database)

        assertTrue(tableExists("pattern_hash"))
        database.query("SELECT * FROM `pattern_hash`").use { assertEquals(0, it.count) }
    }

    private fun tableExists(name: String): Boolean =
        database.query("SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?", arrayOf(name))
            .use { it.count == 1 }
}
