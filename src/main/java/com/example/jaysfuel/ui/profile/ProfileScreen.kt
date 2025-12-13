package com.example.jaysfuel.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.model.RedeemedRewardEntity
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager

/**
 * Profile screen: Shows and edits name, car, birthday. Shows avatar and lets user pick new one (fixed with click and popup). Shows redeemed rewards (tap for QR code). Shows history from Room database (for Milestone 3).
 */
@Composable
fun ProfileScreen(
    onCouponClick: (RewardItem) -> Unit = {}
) {

    var localName by remember { mutableStateOf(UserManager.name) }
    var localCar by remember { mutableStateOf(UserManager.carModel) }
    var localBirthday by remember { mutableStateOf(UserManager.birthday) }

    // New: Controls if avatar picker popup shows.
    var showAvatarDialog by remember { mutableStateOf(false) }

    val history by UserManager.redeemedHistory.collectAsState(initial = emptyList())

    // 优化: 显示 redeemedRewards 列表 (in-memory, from UserManager public getter)
    val redeemedRewards = UserManager.publicRedeemedRewards

    // New: Avatar picker popup (fixed: ensure 5 avatars).
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Choose Avatar") },
            text = {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(UserManager.avatarChoices) { index, resId ->
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "Avatar $index",
                            modifier = Modifier
                                .size(60.dp)
                                .clickable {
                                    UserManager.setAvatar(index)  // Update UserManager state, UI auto refreshes
                                    showAvatarDialog = false
                                }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {

        // Fixed: Add click to avatar image.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = UserManager.avatarResId),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(72.dp)
                    .clickable { showAvatarDialog = true }  // New: Click to show popup
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = UserManager.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = UserManager.carModel,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = UserManager.birthday,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Edit form (keep as is)
        OutlinedTextField(
            value = localName,
            onValueChange = { localName = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = localCar,
            onValueChange = { localCar = it },
            label = { Text("Car model") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = localBirthday,
            onValueChange = { localBirthday = it },
            label = { Text("Birthday") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Fixed/New: Save profile button (was missing or cut off, now full).
        Button(
            onClick = {
                UserManager.updateProfile(localName, localCar, localBirthday)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save profile")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 优化: Redeemed rewards section - 显示动态列表 (if empty, show message)
        Text(
            text = "Redeemed rewards (tap to show QR)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (redeemedRewards.isEmpty()) {
            Text(
                text = "No active coupons yet. Redeem a reward in the Rewards tab.",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            LazyColumn(  // New: Use LazyColumn to show list of redeemed rewards
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)  // Limit height to avoid overflow
            ) {
                items(redeemedRewards) { reward ->
                    CouponCard(
                        reward = reward,
                        onClick = { onCouponClick(reward) }  // Click to navigate to QR screen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Reward history section (keep as is).
        Text(
            text = "Reward history (saved in Room)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "This list shows all rewards ever redeemed on this device.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (history.isEmpty()) {
            Text(
                text = "History is empty.",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            history.forEach { entity ->
                HistoryCard(entity = entity)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// CouponCard and HistoryCard keep as is (copied from your code).
@Composable
private fun CouponCard(
    reward: RewardItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reward.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reward.pointsCost} pts",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to show QR code in the shop.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun HistoryCard(
    entity: RedeemedRewardEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = entity.rewardName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${entity.pointsCost} pts",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Redeemed at: ${entity.redeemedAt}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}