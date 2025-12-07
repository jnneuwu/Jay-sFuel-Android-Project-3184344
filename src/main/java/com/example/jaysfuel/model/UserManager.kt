package com.example.jaysfuel.model

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaysfuel.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Central user state manager.
 *
 * - Holds profile info and avatar
 * - Holds current points and redeemed rewards (in memory)
 * - Writes redeemed history into Room for Milestone 3
 * - Exposes a Flow for history so Compose screens can observe it
 * - Provides selectCoupon / clearCurrentCoupon so the QR screen can work
 */
object UserManager {

    // ---------------------------------------------------------
    // Coroutine scope for Room work
    // ---------------------------------------------------------
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ---------------------------------------------------------
    // Profile data
    // ---------------------------------------------------------

    var name: String by mutableStateOf("Guest")
        private set

    var carModel: String by mutableStateOf("No car yet")
        private set

    var birthday: String by mutableStateOf("Not set")
        private set

    // ---------------------------------------------------------
    // Avatar handling (5 avatars in drawable)
    // ---------------------------------------------------------

    private val avatarResIds: List<Int> = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5
    )

    private var avatarIndex by mutableIntStateOf(0)

    @get:DrawableRes
    val avatarResId: Int
        get() = avatarResIds[avatarIndex.coerceIn(0, avatarResIds.size - 1)]

    /**
     * All available avatar ids, used by the avatar picker UI.
     */
    val avatarChoices: List<Int>
        get() = avatarResIds

    /**
     * Change current avatar by index (0..4).
     */
    fun setAvatar(index: Int) {
        avatarIndex = index.coerceIn(0, avatarResIds.size - 1)
    }

    /**
     * Called from ProfileScreen when user presses "Save profile".
     */
    fun updateProfile(name: String, car: String, birthday: String) {
        this.name = name
        this.carModel = car
        this.birthday = birthday
    }

    // ---------------------------------------------------------
    // Points and in–memory rewards
    // ---------------------------------------------------------

    var points: Int by mutableIntStateOf(1200)
        private set

    // Redeemed rewards kept in memory so Profile and QR screens can show them
    val redeemedRewards = mutableStateListOf<RewardItem>()

    /**
     * Check if user has enough points to redeem a reward.
     */
    fun canRedeem(reward: RewardItem): Boolean = points >= reward.pointsCost

    /**
     * Redeem a reward:
     * - if points not enough → return false, nothing changes
     * - if enough:
     *      - deduct points
     *      - add to in–memory redeemedRewards
     *      - insert a record into Room history (Milestone 3)
     *
     * Returns true when redemption succeeds.
     */
    fun redeemReward(reward: RewardItem): Boolean {
        if (!canRedeem(reward)) {
            return false
        }

        points -= reward.pointsCost
        redeemedRewards.add(reward)
        insertHistoryRecord(reward)

        return true
    }

    /**
     * Small wrapper for the UI.
     * RewardsScreen calls UserManager.redeem(reward),
     * but the main logic is in redeemReward().
     */
    fun redeem(reward: RewardItem): Boolean {
        return redeemReward(reward)
    }

    // ---------------------------------------------------------
    // Room database: redeemed history
    // ---------------------------------------------------------

    private var repository: RewardHistoryRepository? = null
    private var isInitialized: Boolean = false

    // Flow observed by ProfileScreen to show "total rewards in history"
    private val _redeemedHistory =
        MutableStateFlow<List<RedeemedRewardEntity>>(emptyList())
    val redeemedHistory: StateFlow<List<RedeemedRewardEntity>> =
        _redeemedHistory.asStateFlow()

    /**
     * Must be called once from MainActivity.onCreate(applicationContext).
     * Sets up Room and starts observing the history table.
     */
    fun init(context: Context) {
        if (isInitialized) return

        val db = JaysFuelDatabase.getInstance(context)
        repository = RewardHistoryRepository(dao = db.rewardHistoryDao())
        isInitialized = true

        // Observe history from Room and push to StateFlow
        ioScope.launch {
            repository
                ?.observeHistory()
                ?.collectLatest { list ->
                    _redeemedHistory.value = list
                }
        }
    }

    /**
     * Insert a new redeemed record into Room.
     * Called whenever redeemReward() succeeds.
     */
    private fun insertHistoryRecord(reward: RewardItem) {
        val repo = repository ?: return

        val entity = RedeemedRewardEntity(
            id = 0, // auto-generated by Room
            rewardName = reward.name,
            pointsCost = reward.pointsCost,
            redeemedAt = System.currentTimeMillis()
        )

        ioScope.launch {
            repo.insert(entity)
        }
    }

    // ---------------------------------------------------------
    // Coupon selection for QR screen
    // ---------------------------------------------------------

    /**
     * In-memory selected coupon used by CouponQrScreen.
     * AppNavHost calls selectCoupon() and then navigates to QR screen.
     * CouponQrScreen reads currentCoupon and then calls clearCurrentCoupon()
     * when user closes the screen.
     */
    private var _currentCoupon: RewardItem? = null

    val currentCoupon: RewardItem?
        get() = _currentCoupon

    /**
     * Called from AppNavHost when user taps a reward and goes to QR screen.
     */
    fun selectCoupon(reward: RewardItem) {
        _currentCoupon = reward
    }

    /**
     * Called from CouponQrScreen when user closes the QR view.
     */
    fun clearCurrentCoupon() {
        _currentCoupon = null
    }
}
