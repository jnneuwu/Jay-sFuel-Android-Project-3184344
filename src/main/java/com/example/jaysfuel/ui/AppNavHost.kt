package com.example.jaysfuel.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jaysfuel.model.UserManager
import com.example.jaysfuel.ui.coupon.CouponQrScreen
import com.example.jaysfuel.ui.home.HomeScreen
import com.example.jaysfuel.ui.profile.ProfileScreen
import com.example.jaysfuel.ui.rewards.RewardsScreen

/**
 * Navigation graph using Navigation-Compose.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    lux: Float,
    isNightMode: Boolean,
    onToggleTheme: () -> Unit,
    onOpenGasStations: () -> Unit,
    onOpenScanQr: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                lux = lux,
                isNightMode = isNightMode,
                onToggleTheme = onToggleTheme,
                onOpenGasStations = onOpenGasStations,
                onOpenScanQr = onOpenScanQr
            )
        }
        composable(Screen.Rewards.route) {
            RewardsScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onCouponClick = { reward ->
                    // Save selected coupon then navigate to QR screen
                    UserManager.selectCoupon(reward)
                    navController.navigate(Screen.CouponQr.route)
                }
            )
        }
        composable(Screen.CouponQr.route) {
            CouponQrScreen(
                onBack = {
                    navController.popBackStack()
                    UserManager.clearCurrentCoupon()
                }
            )
        }
    }
}
