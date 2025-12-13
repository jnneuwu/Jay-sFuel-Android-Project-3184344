package com.example.jaysfuel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for points refill updates (simulates earning points after fueling).
 */
@Entity(tableName = "points_updates")
data class PointsUpdateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val pointsAdded: Int,
    val timestamp: Long = System.currentTimeMillis()
)