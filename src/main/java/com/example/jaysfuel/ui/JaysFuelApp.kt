package com.example.jaysfuel.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jaysfuel.ui.theme.JaysFuelTheme

/**
 * Root composable for the whole application.
 * It provides the theme and the bottom navigation bar.
 */
@Composable
fun JaysFuelApp(
    lux: Float,
    isNightMode: Boolean,
    onToggleTheme: () -> Unit,
    onOpenGasStations: () -> Unit,
    onOpenScanQr: () -> Unit
) {
    JaysFuelTheme(darkTheme = isNightMode) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavBar(navController = navController)
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                lux = lux,
                isNightMode = isNightMode,
                onToggleTheme = onToggleTheme,
                onOpenGasStations = onOpenGasStations,
                onOpenScanQr = onOpenScanQr
            )
        }
    }
}

/**
 * Simple description of each screen for the bottom navigation bar.
 */
sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Fuel", Icons.Filled.Home)
    object Rewards : Screen("rewards", "Rewards", Icons.Filled.Star)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)

    // This screen is NOT in the bottom bar; it is opened from Profile.
    object CouponQr : Screen("coupon_qr", "Coupon QR", Icons.Filled.Star)
}

/**
 * Bottom navigation bar using Material3 NavigationBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Rewards,
        Screen.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(text = screen.label)
                }
            )
        }
    }
}
