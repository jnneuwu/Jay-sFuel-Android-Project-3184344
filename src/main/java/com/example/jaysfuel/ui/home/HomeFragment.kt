package com.example.jaysfuel.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.jaysfuel.R
import com.example.jaysfuel.theme.ThemeManager

/**
 * Home screen that:
 *  - shows basic fuel prices
 *  - reads the ambient light sensor
 *  - can manually toggle day/night colors with a button
 */
class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var rootLayout: View

    // Cards (frames)
    private lateinit var homeHeaderCard: View
    private lateinit var lightCard: View
    private lateinit var priceCard95: View
    private lateinit var priceCardDiesel: View

    // Text views
    private lateinit var homeTitleTextView: TextView
    private lateinit var homeSubtitleTextView: TextView
    private lateinit var lightTitleTextView: TextView
    private lateinit var lightLevelTextView: TextView
    private lateinit var lightModeTextView: TextView
    private lateinit var toggleThemeButton: Button

    private lateinit var label95TextView: TextView
    private lateinit var price95TextView: TextView
    private lateinit var labelDieselTextView: TextView
    private lateinit var priceDieselTextView: TextView

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null

    private val nightThresholdLux = 50f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootLayout = view.findViewById(R.id.home_root)
        homeHeaderCard = view.findViewById(R.id.home_header)
        lightCard = view.findViewById(R.id.home_light_card)
        priceCard95 = view.findViewById(R.id.cardPrice95)
        priceCardDiesel = view.findViewById(R.id.cardPriceDiesel)

        homeTitleTextView = view.findViewById(R.id.tvHomeTitle)
        homeSubtitleTextView = view.findViewById(R.id.tvHomeSubtitle)
        lightTitleTextView = view.findViewById(R.id.tvLightTitle)
        lightLevelTextView = view.findViewById(R.id.tvLightLevel)
        lightModeTextView = view.findViewById(R.id.tvLightMode)
        toggleThemeButton = view.findViewById(R.id.btnToggleTheme)

        label95TextView = view.findViewById(R.id.tvLabel95)
        price95TextView = view.findViewById(R.id.tvPrice95)
        labelDieselTextView = view.findViewById(R.id.tvLabelDiesel)
        priceDieselTextView = view.findViewById(R.id.tvPriceDiesel)

        val price95 = 1.80f
        val priceDiesel = 1.60f
        price95TextView.text = String.format("%.2f €/L", price95)
        priceDieselTextView.text = String.format("%.2f €/L", priceDiesel)

        applyThemeToViews()
        setupLightSensor()

        toggleThemeButton.setOnClickListener {
            ThemeManager.toggleMode()
            val modeText = if (ThemeManager.isNightMode) {
                "Current mode: Night (manual)"
            } else {
                "Current mode: Day (manual)"
            }
            lightModeTextView.text = modeText
            applyThemeToViews()
        }
    }

    private fun setupLightSensor() {
        val context = requireContext()
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            lightLevelTextView.text = "Light sensor not available (probably emulator)"
            if (!ThemeManager.isNightMode) {
                lightModeTextView.text = "Current mode: Day (default)"
            }
        } else {
            lightLevelTextView.text = "Waiting for sensor..."
        }
    }

    override fun onResume() {
        super.onResume()
        applyThemeToViews()
        lightSensor?.let { sensor ->
            sensorManager?.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            updateThemeFromLux(lux)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this demo.
    }

    private fun updateThemeFromLux(lux: Float) {
        val isNight = lux < nightThresholdLux

        ThemeManager.updateMode(isNight)

        val modeText = if (isNight) {
            "Current mode: Night (sensor)"
        } else {
            "Current mode: Day (sensor)"
        }
        lightModeTextView.text = modeText
        lightLevelTextView.text = String.format("%.0f lux", lux)

        applyThemeToViews()
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)

        ThemeManager.applyCardBackground(
            homeHeaderCard,
            lightCard,
            priceCard95,
            priceCardDiesel
        )

        ThemeManager.applyPrimaryText(
            homeTitleTextView,
            lightTitleTextView,
            label95TextView,
            labelDieselTextView
        )

        ThemeManager.applySecondaryText(
            homeSubtitleTextView,
            lightLevelTextView,
            lightModeTextView
        )

        ThemeManager.applyAccentText(
            price95TextView,
            priceDieselTextView
        )
    }
}
