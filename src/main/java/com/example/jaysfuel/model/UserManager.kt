package com.example.jaysfuel.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaysfuel.R

/**
 * Simple in-memory user data manager for the demo app.
 * It keeps points, profile info, redeemed rewards and the currently
 * selected coupon for the QR screen.
 */
object UserManager {

    // ----- Points and rewards -----

    var points by mutableStateOf(1200)
        private set

    // List of redeemed rewards
    val redeemedRewards = mutableStateListOf<RewardItem>()

    // Currently selected coupon for the QR screen
    var currentCoupon: RewardItem? by mutableStateOf(null)
        private set

    /**
     * Try to redeem a reward. Returns true if successful.
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
     * Add some points (could be called when user buys fuel).
     */
    fun addPoints(amount: Int) {
        points += amount
    }

    /**
     * Select a coupon to be displayed on the CouponQrScreen.
     */
    fun selectCoupon(reward: RewardItem) {
        currentCoupon = reward
    }

    /**
     * Clear the currently selected coupon when leaving the QR screen.
     */
    fun clearCurrentCoupon() {
        currentCoupon = null
    }

    // ----- Profile info -----

    var name by mutableStateOf("Mingyu")
    var carModel by mutableStateOf("Unknown car")
    var birthday by mutableStateOf("")

    // Avatar resource id (default to first avatar image)
    var avatarResId by mutableStateOf(R.drawable.avatar_1)
        private set

    fun setAvatar(resId: Int) {
        avatarResId = resId
    }
}
