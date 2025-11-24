package com.example.jaysfuel

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val nightThresholdLux = 50f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init light sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        setContent {
            JaysFuelApp(
                lux = luxState,
                isNightMode = isNightModeState,
                onToggleTheme = {
                    // Manual toggle for emulator / demo
                    isNightModeState = !isNightModeState
                },
                onOpenGasStations = { openGasStations() },
                onOpenScanQr = { openScanQr() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            luxState = lux
            // Automatically switch theme based on light level
            isNightModeState = lux < nightThresholdLux
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    /**
     * Open nearby gas stations in a map app.
     */
    private fun openGasStations() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=petrol+station")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
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
