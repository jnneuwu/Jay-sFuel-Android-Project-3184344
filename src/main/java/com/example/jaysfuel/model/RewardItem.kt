package com.example.jaysfuel.model

/**
 * Represents one reward item that can be redeemed with points.
 */
data class RewardItem(
    val id: Int,
    val name: String,
    val description: String,
    val pointsCost: Int,
    val category: String
)
