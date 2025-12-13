package com.example.jaysfuel.model

import android.content.Context
import android.util.Log
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
 * Main manager for user info. Holds profile and avatar. Holds points and rewards in memory. Saves history to Room for Milestone 3. Shares history with screens. Handles coupon pick for QR screen. New: Saves/loads profile to UserStorage (name, car, birthday, avatar, points).
 */
object UserManager {

    // Coroutine scope for Room work
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Profile data
    var name: String by mutableStateOf("Guest")
        private set

    var carModel: String by mutableStateOf("No car yet")
        private set

    var birthday: String by mutableStateOf("Not set")
        private set

    // Avatar handling (5 avatars in drawable)
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
     * Change avatar by number (0 to 4). New: Save to Storage.
     */
    fun setAvatar(index: Int) {
        val clampedIndex = index.coerceIn(0, avatarResIds.size - 1)
        if (avatarIndex != clampedIndex) {
            avatarIndex = clampedIndex
            // Save to Storage (async, no block UI)
            saveToStorage()
        }
    }

    /**
     * Called when user saves profile in screen. New: Save to Storage.
     */
    fun updateProfile(name: String, car: String, birthday: String) {
        this.name = name
        this.carModel = car
        this.birthday = birthday
        // Save to Storage
        saveToStorage()
    }

    // Points and rewards
    var points: Int by mutableIntStateOf(1200)
        private set

    private val redeemedRewards = mutableStateListOf<RewardItem>()

    // FIXED: Renamed to avoid name conflict with private val; public for ProfileScreen
    val publicRedeemedRewards: List<RewardItem> get() = redeemedRewards

    // New: UserStorage instance (lazy start).
    private var userStorage: UserStorage? = null

    private fun getUserStorage(context: Context): UserStorage {
        return userStorage ?: UserStorage(context).also { userStorage = it }
    }

    /**
     * New: Save profile to SharedPreferences (async, no block UI).
     */
    private fun saveToStorage(context: Context? = null) {
        val storage = if (context != null) UserStorage(context) else userStorage ?: return
        ioScope.launch {
            storage.saveName(name)
            storage.saveCarModel(carModel)
            storage.saveBirthday(birthday)
            storage.saveAvatarIndex(avatarIndex)
            storage.savePoints(points)
        }
    }

    /**
     * New: Load profile from Storage (sync, call on start).
     */
    private fun loadFromStorage(context: Context) {
        val storage = getUserStorage(context)
        name = storage.loadName()
        carModel = storage.loadCarModel()
        birthday = storage.loadBirthday()
        avatarIndex = storage.loadAvatarIndex()
        points = storage.loadPoints()
    }

    // Redeem logic (keep as is, short).
    private fun canRedeem(reward: RewardItem): Boolean = points >= reward.pointsCost

    private fun redeemReward(reward: RewardItem): Boolean {
        if (!canRedeem(reward)) {
            return false
        }

        points -= reward.pointsCost
        redeemedRewards.add(reward)
        insertHistoryRecord(reward)
        // New: Save points to Storage
        saveToStorage()

        return true
    }

    fun redeem(reward: RewardItem): Boolean {
        return redeemReward(reward)
    }

    // FIXED: New method for refilling points (simulate earning after fueling)
    fun refillPoints(newPoints: Int = 1200) {
        val added = newPoints - points
        points = newPoints
        insertPointsUpdateRecord(added)
        saveToStorage()
    }

    // FIXED: Insert points update record to Room
    private fun insertPointsUpdateRecord(added: Int) {
        val repo = pointsUpdateRepository ?: return
        val entity = PointsUpdateEntity(
            pointsAdded = added,
            timestamp = System.currentTimeMillis()
        )
        ioScope.launch {
            repo.insert(entity)
        }
    }

    // Room database: history section (keep as is).
    private var repository: RewardHistoryRepository? = null
    private var pointsUpdateRepository: PointsUpdateRepository? = null  // FIXED: Add for points updates
    private var isInitialized: Boolean = false

    private val _redeemedHistory =
        MutableStateFlow<List<RedeemedRewardEntity>>(emptyList())
    val redeemedHistory: StateFlow<List<RedeemedRewardEntity>> =
        _redeemedHistory.asStateFlow()

    /**
     * Call once from MainActivity start. Sets up Room and watches history. New: Loads profile from Storage.
     */
    fun init(context: Context) {
        if (isInitialized) return

        // New: Load saved profile first
        loadFromStorage(context)

        val db = JaysFuelDatabase.getInstance(context)
        repository = RewardHistoryRepository(dao = db.rewardHistoryDao())
        pointsUpdateRepository = PointsUpdateRepository(dao = db.pointsUpdateDao())  // FIXED: Init points repo
        isInitialized = true

        // Watch history from Room and push to StateFlow
        ioScope.launch {
            try {  // FIXED: Add try-catch to prevent Room exceptions from crashing app
                repository
                    ?.observeHistory()
                    ?.collectLatest { list ->
                        _redeemedHistory.value = list
                    }
            } catch (e: Exception) {
                Log.e("UserManager", "Room history observe failed", e)  // FIXED: Log error, no crash
            }
        }
    }

    private fun insertHistoryRecord(reward: RewardItem) {
        val repo = repository ?: return

        val entity = RedeemedRewardEntity(
            id = 0, // auto-generated by Room
            rewardName = reward.name,
            pointsCost = reward.pointsCost,
            redeemedAt = System.currentTimeMillis()
        )

        ioScope.launch {
            try {
                repo.insert(entity)
            } catch (e: Exception) {
                Log.e("UserManager", "Room insert failed", e)  // FIXED: Catch insert error
            }
        }
    }

    // Coupon pick for QR screen (keep as is).
    private var _currentCoupon: RewardItem? = null

    val currentCoupon: RewardItem?
        get() = _currentCoupon

    fun selectCoupon(reward: RewardItem) {
        _currentCoupon = reward
    }

    fun clearCurrentCoupon() {
        _currentCoupon = null
    }
}