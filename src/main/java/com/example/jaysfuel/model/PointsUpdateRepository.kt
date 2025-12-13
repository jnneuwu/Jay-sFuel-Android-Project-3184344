package com.example.jaysfuel.model

import kotlinx.coroutines.flow.Flow

/**
 * Repository for points updates.
 */
class PointsUpdateRepository(
    private val dao: PointsUpdateDao
) {

    fun observeAll(): Flow<List<PointsUpdateEntity>> = dao.observeAll()

    suspend fun insert(entity: PointsUpdateEntity) {
        dao.insert(entity)
    }
}