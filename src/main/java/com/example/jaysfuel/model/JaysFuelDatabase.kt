package com.example.jaysfuel.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database used for Milestone 3.
 * Right now it only stores reward history.
 */
@Database(
    entities = [RedeemedRewardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JaysFuelDatabase : RoomDatabase() {

    abstract fun rewardHistoryDao(): RewardHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: JaysFuelDatabase? = null

        fun getInstance(context: Context): JaysFuelDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    JaysFuelDatabase::class.java,
                    "jays_fuel.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
