package com.example.jaysfuel.model

import android.content.Context

/**
 * Simple persistence layer based on SharedPreferences.
 * Data is stored in a private XML file on the device.
 *
 * It keeps the basic user data:
 * - name
 * - car model
 * - birthday
 * - avatar index
 * - current points
 */
class UserStorage(context: Context) {

    private val prefs = context.getSharedPreferences("jaysfuel_user", Context.MODE_PRIVATE)

    fun loadName(): String =
        prefs.getString(KEY_NAME, DEFAULT_NAME) ?: DEFAULT_NAME

    fun saveName(name: String) {
        prefs.edit().putString(KEY_NAME, name).apply()
    }

    fun loadCarModel(): String =
        prefs.getString(KEY_CAR_MODEL, DEFAULT_CAR_MODEL) ?: DEFAULT_CAR_MODEL

    fun saveCarModel(carModel: String) {
        prefs.edit().putString(KEY_CAR_MODEL, carModel).apply()
    }

    fun loadBirthday(): String =
        prefs.getString(KEY_BIRTHDAY, DEFAULT_BIRTHDAY) ?: DEFAULT_BIRTHDAY

    fun saveBirthday(birthday: String) {
        prefs.edit().putString(KEY_BIRTHDAY, birthday).apply()
    }

    fun loadAvatarIndex(): Int =
        prefs.getInt(KEY_AVATAR_INDEX, DEFAULT_AVATAR_INDEX)

    fun saveAvatarIndex(index: Int) {
        prefs.edit().putInt(KEY_AVATAR_INDEX, index).apply()
    }

    fun loadPoints(): Int =
        prefs.getInt(KEY_POINTS, DEFAULT_POINTS)

    fun savePoints(points: Int) {
        prefs.edit().putInt(KEY_POINTS, points).apply()
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_CAR_MODEL = "car_model"
        private const val KEY_BIRTHDAY = "birthday"
        private const val KEY_AVATAR_INDEX = "avatar_index"
        private const val KEY_POINTS = "points"

        // Default values for a fresh install
        private const val DEFAULT_NAME = "Guest"
        private const val DEFAULT_CAR_MODEL = "Unknown car"
        private const val DEFAULT_BIRTHDAY = "Not set"
        private const val DEFAULT_AVATAR_INDEX = 0
        private const val DEFAULT_POINTS = 1200
    }
}
