package com.example.jaysfuel.ui.rewards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager
import kotlinx.coroutines.launch

/**
 * Rewards screen:
 *  - Shows current points
 *  - Shows a list of redeemable rewards with icons
 *  - Allows redeeming rewards, updating points and redeemed list
 */
@Composable
fun RewardsScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val rewardItems = remember { createRewardItems() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)

        // Points header
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Your points",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${UserManager.points} pts",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Available rewards",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(rewardItems) { item ->
                RewardCard(
                    reward = item,
                    onRedeem = { reward ->
                        val success = UserManager.redeem(reward)
                        if (success) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Redeemed: ${reward.name}")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Not enough points")
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: RewardItem,
    onRedeem: (RewardItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                val iconRes = when (reward.category) {
                    "Fuel" -> R.drawable.ic_gas
                    "Drink" -> R.drawable.ic_gift
                    "Snack" -> R.drawable.ic_gift
                    "Service" -> R.drawable.ic_navigation
                    else -> R.drawable.ic_star
                }

                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = reward.name,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Column {
                    Text(
                        text = reward.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reward.pointsCost} pts",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onRedeem(reward) }) {
                Text("REDEEM")
            }
        }
    }
}

private fun createRewardItems(): List<RewardItem> {
    return listOf(
        RewardItem(
            id = 1,
            name = "€5 Fuel Coupon",
            description = "Save €5 on your next fuel purchase.",
            pointsCost = 500,
            category = "Fuel"
        ),
        RewardItem(
            id = 2,
            name = "€10 Fuel Coupon",
            description = "Save €10 on fuel at participating stations.",
            pointsCost = 900,
            category = "Fuel"
        ),
        RewardItem(
            id = 3,
            name = "Free Coffee",
            description = "One free hot coffee at the shop.",
            pointsCost = 200,
            category = "Drink"
        ),
        RewardItem(
            id = 4,
            name = "Cold Drink Combo",
            description = "Soft drink plus small snack.",
            pointsCost = 350,
            category = "Drink"
        ),
        RewardItem(
            id = 5,
            name = "Car Wash Voucher",
            description = "Standard car wash at the station.",
            pointsCost = 600,
            category = "Service"
        ),
        RewardItem(
            id = 6,
            name = "Snack Pack",
            description = "Chips and chocolate bar bundle.",
            pointsCost = 300,
            category = "Snack"
        ),
        RewardItem(
            id = 7,
            name = "Premium Fuel Upgrade",
            description = "Upgrade to premium fuel for one fill.",
            pointsCost = 750,
            category = "Fuel"
        ),
        RewardItem(
            id = 8,
            name = "Energy Drink",
            description = "One free energy drink can.",
            pointsCost = 250,
            category = "Drink"
        )
    )
}
