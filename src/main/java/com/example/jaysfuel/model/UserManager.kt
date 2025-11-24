package com.example.jaysfuel.model

/**
 * Simple in-memory user data manager.
 * Stores current points and redeemed rewards for the demo.
 */
object UserManager {

    /** Total points that the user currently has. */
    var points: Int = 2500

    /** All rewards that the user has successfully redeemed. */
    val redeemedRewards: MutableList<RewardItem> = mutableListOf()

    /**
     * Check whether the user has enough points to redeem a reward.
     */
    fun canRedeem(item: RewardItem): Boolean {
        return points >= item.pointsCost
    }

    /**
     * Try to redeem a reward. Returns true if success, false otherwise.
     */
    fun redeem(item: RewardItem): Boolean {
        return if (canRedeem(item)) {
            points -= item.pointsCost
            redeemedRewards.add(item)
            true
        } else {
            false
        }
    }
}
