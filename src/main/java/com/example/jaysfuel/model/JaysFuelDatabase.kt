package com.example.jaysfuel.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room database used for Milestone 3.
 * Right now it only stores reward history.
 * FIXED: Added PointsUpdateEntity for refill logs.
 */
@Database(
    entities = [RedeemedRewardEntity::class, PointsUpdateEntity::class],
    version = 2,
    exportSchema = false
)
abstract class JaysFuelDatabase : RoomDatabase() {

    abstract fun rewardHistoryDao(): RewardHistoryDao
    abstract fun pointsUpdateDao(): PointsUpdateDao

    companion object {
        @Volatile
        private var INSTANCE: JaysFuelDatabase? = null

        fun getInstance(context: Context): JaysFuelDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    JaysFuelDatabase::class.java,
                    "jays_fuel.db"
                ).addMigrations(MIGRATION_1_2)  // FIXED: Add manual migration for version 2
                    .build().also { INSTANCE = it }
            }
        }
    }
}

// FIXED: Manual migration from version 1 to 2 (create points_updates table)
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `points_updates` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `pointsAdded` INTEGER NOT NULL,
                `timestamp` INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))
            )
        """.trimIndent())
    }
}