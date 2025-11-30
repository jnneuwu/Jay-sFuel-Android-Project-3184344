package com.example.jaysfuel.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.jaysfuel.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Home/Fuel screen in Compose.
 * It shows:
 * - A description of the app goal
 * - Ambient light information and theme toggle
 * - A simple fuel price chart and current prices
 * - Quick actions (open map / open scan QR)
 */
@Composable
fun HomeScreen(
    lux: Float,
    isNightMode: Boolean,
    onToggleTheme: () -> Unit,
    onOpenGasStations: () -> Unit,
    onOpenScanQr: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top card with the main app goal sentence
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Fuel prices today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Track fuel prices, earn points, and redeem coupons to save money on your trips.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Ambient light and theme card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Ambient light",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                val luxText = if (lux < 0f) {
                    "Waiting for sensor data (or running on emulator)."
                } else {
                    "${lux.toInt()} lux"
                }

                Text(
                    text = luxText,
                    style = MaterialTheme.typography.bodyMedium
                )

                val modeText = if (isNightMode) "Night" else "Day"
                Text(
                    text = "Current mode: $modeText",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onToggleTheme,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("TOGGLE THEME")
                }
            }
        }

        // Fuel price trend + current prices
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Fuel price trend",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FuelPriceChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                PriceRow(label = "95 Octane", price = "1.80 €/L")
                PriceRow(label = "Diesel", price = "1.60 €/L")
            }
        }

        // Quick actions
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Quick actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = onOpenGasStations,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("FIND NEARBY GAS STATIONS")
                }

                Button(
                    onClick = onOpenScanQr,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SCAN QR CODE")
                }
            }
        }
    }
}

/**
 * Small row that shows a fuel type and its price.
 */
@Composable
private fun PriceRow(
    label: String,
    price: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = price,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * MPAndroidChart line chart embedded into Compose with AndroidView.
 * Uses fixed sample data for demonstration.
 */
@Composable
private fun FuelPriceChart(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                setTouchEnabled(true)
                setPinchZoom(true)
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val entries95 = listOf(
                Entry(1f, 1.80f),
                Entry(2f, 1.82f),
                Entry(3f, 1.78f),
                Entry(4f, 1.81f),
                Entry(5f, 1.80f)
            )

            val entriesDiesel = listOf(
                Entry(1f, 1.60f),
                Entry(2f, 1.61f),
                Entry(3f, 1.59f),
                Entry(4f, 1.60f),
                Entry(5f, 1.62f)
            )

            val set95 = LineDataSet(entries95, "95 Octane").apply {
                lineWidth = 2f
                circleRadius = 3f
            }

            val setDiesel = LineDataSet(entriesDiesel, "Diesel").apply {
                lineWidth = 2f
                circleRadius = 3f
            }

            chart.data = LineData(set95, setDiesel)
            chart.invalidate()
        }
    )
}
