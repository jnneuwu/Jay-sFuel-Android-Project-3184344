package com.example.jaysfuel.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.R
import com.example.jaysfuel.model.RewardItem
import com.example.jaysfuel.model.UserManager

/**
 * Profile screen:
 *  - Shows avatar and editable profile information (name, birthday, car model)
 *  - Shows total points
 *  - Shows list of redeemed rewards (tap to open QR code screen)
 */
@Composable
fun ProfileScreen(
    onCouponClick: (RewardItem) -> Unit
) {
    val points = UserManager.points
    val redeemed = UserManager.redeemedRewards

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header card with avatar and basic info
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Image(
                        painter = painterResource(id = UserManager.avatarResId),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .height(64.dp)
                            .padding(end = 16.dp)
                    )
                    Column {
                        Text(
                            text = UserManager.userName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Car: ${UserManager.carModel}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Birthday: ${UserManager.birthday}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { UserManager.toggleAvatar() }) {
                    Text("CHANGE AVATAR")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable profile fields card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Edit profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = UserManager.userName,
                    onValueChange = { UserManager.userName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = UserManager.birthday,
                    onValueChange = { UserManager.birthday = it },
                    label = { Text("Birthday (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = UserManager.carModel,
                    onValueChange = { UserManager.carModel = it },
                    label = { Text("Car model") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Points card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total points",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$points pts",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Redeemed rewards list
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Redeemed rewards",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (redeemed.isEmpty()) {
                    Text(
                        text = "You have not redeemed any rewards yet.",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    LazyColumn {
                        items(redeemed) { reward ->
                            RewardRow(
                                reward = reward,
                                onClick = { onCouponClick(reward) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardRow(
    reward: RewardItem,
    onClick: () -> Unit
) {
    // Simple clickable row: for now we use a Button-like style
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                text = reward.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${reward.category} • ${reward.pointsCost} pts",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
