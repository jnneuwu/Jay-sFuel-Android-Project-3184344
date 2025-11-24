package com.example.jaysfuel.model

/**
 * Model for a reward item that can be redeemed with points.
 */
data class RewardItem(
    val id: Int,
    val name: String,
    val description: String,
    val pointsCost: Int,
    val imageResId: Int,
    val category: String // e.g. "Fuel", "Drink", "Snack"
)
