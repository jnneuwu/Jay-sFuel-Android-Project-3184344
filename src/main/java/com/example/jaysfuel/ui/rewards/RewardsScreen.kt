package com.example.jaysfuel.ui.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale  // FIXED: Correct import for ContentScale (UI layout module)
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager

/**
 * Rewards screen.
 * Shows current points and a list of products
 * that the user can redeem.
 */
@Composable
fun RewardsScreen(
    modifier: Modifier = Modifier
) {
    val rewards = remember { demoRewards }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card with total points
        item {
            PointsSummaryCard()
        }

        // FIXED: Add Refill Points button below points card
        item {
            RefillPointsButton()
        }

        // Small promo banner
        item {
            BannerCard()
        }

        // Section title text
        item {
            Text(
                text = "Rewards store",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Use your points to redeem fuel discounts and snacks.",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // List of reward products
        items(rewards) { reward ->
            RewardProductCard(reward = reward)
        }
    }
}

/**
 * Card that shows how many points the user has.
 */
@Composable
private fun PointsSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total points:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${UserManager.points}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * FIXED: New button to refill points to 1200 (simulate earning after fueling), log to Room.
 */
@Composable
private fun RefillPointsButton() {
    Button(
        onClick = { UserManager.refillPoints() },  // FIXED: Call refill method
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = "Refill Points (Simulate Fueling)",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Promo banner with Ferrari image.
 * Tap to open map or something, but for now just static.
 */
@Composable
private fun BannerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ferrari),
                contentDescription = "Ferrari promo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop  // FIXED: Now imported, no error
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Drive like a pro",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Premium fuel at Jay's",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

/**
 * Card for one reward product.
 * Shows image, name, description, points cost.
 * Tap to confirm redeem.
 */
@Composable
private fun RewardProductCard(reward: RewardItem) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var lastRedeemSuccess by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showConfirmDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image based on id
            Image(
                painter = painterResource(id = getImageForRewardId(reward.id)),
                contentDescription = reward.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${reward.pointsCost} pts",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${reward.category})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Confirm dialog (before redeem)
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text("Redeem ${reward.name}")
            },
            text = {
                Text("Are you sure you want to redeem '${reward.name}' for ${reward.pointsCost} points?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Fixed: Use public redeem() only. It checks points and redeems if possible.
                        val success = UserManager.redeem(reward)
                        lastRedeemSuccess = success
                        showConfirmDialog = false
                        showResultDialog = true
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Result dialog (success / not enough points)
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = {
                Text(
                    if (lastRedeemSuccess) "Redeem successful"
                    else "Not enough points"
                )
            },
            text = {
                Text(
                    if (lastRedeemSuccess) {
                        "The reward has been added to your profile."
                    } else {
                        "You do not have enough points to redeem this reward."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { showResultDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

// Helper: Get drawable id based on reward id
private fun getImageForRewardId(id: Int): Int {
    return when (id) {
        1 -> R.drawable.fuel_coupon
        2 -> R.drawable.premium_fuel_upgrade
        3 -> R.drawable.coffee
        4 -> R.drawable.cold_drink_combo
        5 -> R.drawable.snack_pack
        6 -> R.drawable.car_wash_voucher
        7 -> R.drawable.energy_drink
        else -> R.drawable.ic_navigation  // Fixed: Use existing ic_navigation.xml as default
    }
}

// Demo reward data, ids are used to choose images above
private val demoRewards = listOf(
    RewardItem(
        id = 1,
        name = "€5 Fuel Coupon",
        description = "Save €5 on your next fuel purchase.",
        pointsCost = 500,
        category = "Fuel"
    ),
    RewardItem(
        id = 2,
        name = "Premium fuel upgrade",
        description = "Upgrade to premium fuel once.",
        pointsCost = 900,
        category = "Fuel"
    ),
    RewardItem(
        id = 3,
        name = "Free coffee",
        description = "One free coffee at the station shop.",
        pointsCost = 200,
        category = "Drink"
    ),
    RewardItem(
        id = 4,
        name = "Cold drink combo",
        description = "Cold drink and snack combo.",
        pointsCost = 350,
        category = "Drink"
    ),
    RewardItem(
        id = 5,
        name = "Snack pack",
        description = "Mixed snacks for your road trip.",
        pointsCost = 300,
        category = "Snack"
    ),
    RewardItem(
        id = 6,
        name = "Car wash voucher",
        description = "Standard car wash for your car.",
        pointsCost = 400,
        category = "Service"
    ),
    RewardItem(
        id = 7,
        name = "Energy drink",
        description = "Energy drink to keep you awake.",
        pointsCost = 250,
        category = "Drink"
    )
)