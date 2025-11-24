package com.example.jaysfuel.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

/**
 * Home screen:
 *  - Shows fuel prices
 *  - Shows ambient light sensor value and mode
 *  - Provides a manual theme toggle button
 *  - Provides buttons to open gas stations and scan QR
 *  - Displays a fuel price trend chart using MPAndroidChart
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fuel prices today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Track price changes across different fuel types.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fuel price trend chart card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Fuel price trend",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FuelPriceChart()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Light sensor card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ambient light",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                val luxText = if (lux > 0f) {
                    String.format("%.0f lux", lux)
                } else {
                    "Waiting for sensor or running on emulator"
                }

                Text(
                    text = "Current light: $luxText",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                val modeText = if (isNightMode) {
                    "Current mode: Night"
                } else {
                    "Current mode: Day"
                }

                Text(
                    text = modeText,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = onToggleTheme) {
                    Text("TOGGLE THEME")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fuel price card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fuel prices",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                FuelPriceRow(label = "95 Octane", price = 1.80f)
                Spacer(modifier = Modifier.height(4.dp))
                FuelPriceRow(label = "Diesel", price = 1.60f)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions card: open map / scan QR
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Quick actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(onClick = onOpenGasStations) {
                    Text("FIND NEARBY GAS STATIONS")
                }

                Button(onClick = onOpenScanQr) {
                    Text("SCAN QR CODE (placeholder)")
                }
            }
        }
    }
}

@Composable
private fun FuelPriceRow(label: String, price: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = String.format("%.2f €/L", price),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary // accent color (yellow in dark mode)
        )
    }
}

/**
 * Wraps an MPAndroidChart LineChart inside a composable using AndroidView.
 */
@Composable
private fun FuelPriceChart() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                legend.isEnabled = false

                // Sample data for the chart (days vs price)
                val entries = listOf(
                    Entry(0f, 1.75f),
                    Entry(1f, 1.78f),
                    Entry(2f, 1.80f),
                    Entry(3f, 1.82f),
                    Entry(4f, 1.79f)
                )

                val dataSet = LineDataSet(entries, "95 Octane").apply {
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawValues(false)
                }

                data = LineData(dataSet)
                invalidate()
            }
        }
    )
}
