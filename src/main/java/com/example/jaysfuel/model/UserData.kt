package com.example.jaysfuel.model

/**
 * Simple user profile data used for the demo screens.
 */
data class UserData(
    val name: String,
    val email: String,
    val carPlate: String
)

/**
 * Provides a single demo user instance.
 */
object CurrentUser {

    /** Static demo user for the profile screen. */
    val user: UserData = UserData(
        name = "Mingyu",
        email = "mingyu@example.com",
        carPlate = "12-D-3456"
    )
}
