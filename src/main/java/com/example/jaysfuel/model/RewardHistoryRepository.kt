package com.example.jaysfuel.model

import kotlinx.coroutines.flow.Flow

/**
 * Simple repository wrapper around the DAO.
 */
class RewardHistoryRepository(
    private val dao: RewardHistoryDao
) {

    fun observeHistory(): Flow<List<RedeemedRewardEntity>> = dao.observeHistory()

    suspend fun insert(entry: RedeemedRewardEntity) {
        dao.insert(entry)
    }
}
