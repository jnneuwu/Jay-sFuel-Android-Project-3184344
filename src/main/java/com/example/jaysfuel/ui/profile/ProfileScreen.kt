package com.example.jaysfuel.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager

/**
 * Profile screen:
 * - Shows user info and avatar
 * - Allows editing basic fields
 * - Shows redeemed rewards
 * - Allows picking an avatar image
 * - Notifies when a redeemed coupon is clicked
 */
@Composable
fun ProfileScreen(
    onCouponClick: (RewardItem) -> Unit = {}
) {
    var localName by remember { mutableStateOf(UserManager.name) }
    var localCar by remember { mutableStateOf(UserManager.carModel) }
    var localBirthday by remember { mutableStateOf(UserManager.birthday) }

    var showAvatarPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic info + avatar
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = UserManager.avatarResId),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { showAvatarPicker = true }
                        .height(72.dp) // avatar size
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = localName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = localCar,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = localBirthday,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Editable fields
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Edit profile",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = localName,
                    onValueChange = { value ->
                        localName = value
                        UserManager.name = value
                    },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = localCar,
                    onValueChange = { value ->
                        localCar = value
                        UserManager.carModel = value
                    },
                    label = { Text("Car model") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = localBirthday,
                    onValueChange = { value ->
                        localBirthday = value
                        UserManager.birthday = value
                    },
                    label = { Text("Birthday") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Redeemed rewards list
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Redeemed rewards",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Here you can see all the rewards you have already redeemed. Tap one to show a QR code in the shop.",
                    style = MaterialTheme.typography.bodySmall
                )

                if (UserManager.redeemedRewards.isEmpty()) {
                    Text(
                        text = "No rewards redeemed yet.",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    UserManager.redeemedRewards.forEach { reward: RewardItem ->
                        Text(
                            text = "• ${reward.name} (${reward.pointsCost} pts)",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.clickable {
                                onCouponClick(reward)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAvatarPicker) {
        AvatarPickerDialog(
            onAvatarSelected = { resId ->
                UserManager.setAvatar(resId)
                showAvatarPicker = false
            },
            onDismiss = { showAvatarPicker = false }
        )
    }
}

/**
 * Dialog that shows five real avatar images instead of icons.
 * Make sure avatar_1...avatar_5 drawables exist in res/drawable.
 */
@Composable
private fun AvatarPickerDialog(
    onAvatarSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val avatars = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose your avatar") },
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                avatars.forEach { resId ->
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = "Avatar option",
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .clickable { onAvatarSelected(resId) }
                            .height(64.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
