package com.example.jaysfuel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jaysfuel.model.UserManager
import com.example.jaysfuel.ui.JaysFuelApp

/**
 * Main activity using Jetpack Compose as the root UI.
 * It reads the ambient light sensor and passes the data
 * to the composable app.
 */
class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    // Current light level in lux (observable state for Compose)
    private var luxState by mutableFloatStateOf(0f)

    // Whether the app should use night mode (observable state for Compose)
    private var isNightModeState by mutableStateOf(false)

    // Threshold to switch between day and night.
    private val nightThreshold = 10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIXED: Init UserManager with context for Room and Storage
        UserManager.init(this.applicationContext)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        setContent {
            JaysFuelApp(
                lux = luxState,
                isNightMode = isNightModeState,
                onToggleTheme = { isNightModeState = !isNightModeState },
                onOpenGasStations = ::openGasStations,
                onOpenScanQr = ::openScanQr
            )
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            luxState = event.values[0]
            isNightModeState = luxState < nightThreshold
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    /**
     * Open nearby gas stations in a map app or browser.
     * 1. Try geo: URI with any map app.
     * 2. If no map app, try Google Maps web in browser.
     * 3. If both fail, show a Toast.
     */
    private fun openGasStations() {
        // First try a geo: intent (any map app can handle this)
        val geoUri = Uri.parse("geo:0,0?q=petrol+station")
        val geoIntent = Intent(Intent.ACTION_VIEW, geoUri)

        try {
            startActivity(geoIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // No map app found, try browser below
        }

        // Fallback: open Google Maps web in browser
        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=petrol+station")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)

        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            // No app can handle map or browser
            Toast.makeText(this, "No app found to open map.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open the QR scan activity when the user clicks the button.
     * This uses the existing ScanQRActivity based on ZXing.
     */
    private fun openScanQr() {
        val intent = Intent(this, ScanQRActivity::class.java)
        startActivity(intent)
    }
}