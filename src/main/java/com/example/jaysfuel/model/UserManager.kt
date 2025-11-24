package com.example.jaysfuel.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaysfuel.R

/**
 * Simple in-memory user state manager.
 * Holds current points, redeemed rewards and basic profile data.
 */
object UserManager {

    // Points and rewards
    var points by mutableStateOf(1000)
        private set

    val redeemedRewards = mutableStateListOf<RewardItem>()

    // Basic profile information
    var userName by mutableStateOf("Mingyu")
    var birthday by mutableStateOf("1999-01-01")
    var carModel by mutableStateOf("My first car")

    // Avatar handling: cycle through local drawable resources
    private var avatarIndex by mutableStateOf(0)

    private val avatarOptions = listOf(
        R.drawable.ic_person,
        R.drawable.ic_star
    )

    val avatarResId: Int
        get() = avatarOptions[avatarIndex]

    // Currently selected coupon for the QR screen
    var currentCoupon: RewardItem? by mutableStateOf(null)
        private set

    /**
     * Try to redeem a reward. Returns true if success.
     */
    fun redeem(reward: RewardItem): Boolean {
        return if (points >= reward.pointsCost) {
            points -= reward.pointsCost
            redeemedRewards.add(reward)
            true
        } else {
            false
        }
    }

    /**
     * Adds points to the current user.
     */
    fun addPoints(amount: Int) {
        points += amount
    }

    /**
     * Remember which coupon the user wants to show as QR code.
     */
    fun selectCoupon(reward: RewardItem) {
        currentCoupon = reward
    }

    /**
     * Clear the current coupon selection.
     */
    fun clearCurrentCoupon() {
        currentCoupon = null
    }

    /**
     * Change avatar to the next drawable icon.
     */
    fun toggleAvatar() {
        avatarIndex = (avatarIndex + 1) % avatarOptions.size
    }
}
