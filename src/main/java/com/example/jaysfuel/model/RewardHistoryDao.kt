package com.example.jaysfuel.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the reward history table.
 */
@Dao
interface RewardHistoryDao {

    @Query("SELECT * FROM reward_history ORDER BY redeemedAt DESC")
    fun observeHistory(): Flow<List<RedeemedRewardEntity>>

    @Insert
    suspend fun insert(entry: RedeemedRewardEntity)
}

