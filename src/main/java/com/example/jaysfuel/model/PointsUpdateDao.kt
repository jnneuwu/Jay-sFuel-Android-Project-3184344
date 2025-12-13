package com.example.jaysfuel.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO for points updates table.
 */
@Dao
interface PointsUpdateDao {

    @Insert
    suspend fun insert(entity: PointsUpdateEntity)

    @Query("SELECT * FROM points_updates ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<PointsUpdateEntity>>
}