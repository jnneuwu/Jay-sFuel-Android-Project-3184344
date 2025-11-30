package com.example.jaysfuel.ui.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager

/**
 * Rewards screen showing current points and a list of products
 * that can be redeemed with points.
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
        item {
            PointsSummaryCard()
        }

        item {
            BannerCard()
        }

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

        items(rewards) { reward ->
            RewardProductCard(reward = reward)
        }
    }
}

@Composable
private fun PointsSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Your points",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${UserManager.points} pts",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
private fun BannerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cold_drink_combo),
                contentDescription = "Rewards banner illustration",
                modifier = Modifier
                    .height(72.dp)
                    .weight(1f)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Save on every refill",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Redeem fuel coupons and shop snacks with your points.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun RewardProductCard(
    reward: RewardItem
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var lastRedeemSuccess by remember { mutableStateOf(false) }

    val imageRes = when (reward.id) {
        1 -> R.drawable.fuel_coupon
        2 -> R.drawable.premium_fuel_upgrade
        3 -> R.drawable.coffee
        4 -> R.drawable.cold_drink_combo
        5 -> R.drawable.snack_pack
        6 -> R.drawable.car_wash_voucher
        7 -> R.drawable.energy_drink
        else -> R.drawable.fuel_coupon
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = reward.name,
                    modifier = Modifier
                        .height(80.dp)
                        .weight(1f)
                )

                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = reward.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${reward.pointsCost} pts",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "REDEEM")
            }
        }
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Redeem reward") },
            text = {
                Text("Are you sure you want to redeem \"${reward.name}\" for ${reward.pointsCost} points?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
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

    // Result dialog
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { showResultDialog = false },
            title = {
                Text(if (lastRedeemSuccess) "Redeem successful" else "Not enough points")
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
