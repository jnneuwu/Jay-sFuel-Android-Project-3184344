package com.example.jaysfuel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single redeemed reward record stored in Room.
 */
@Entity(tableName = "reward_history")
data class RedeemedRewardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val rewardName: String,
    val pointsCost: Int,
    val redeemedAt: Long
)
