package com.example.jaysfuel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jaysfuel.theme.ThemeManager

/**
 * Gas station screen using ThemeManager for colors.
 */
class GasStationActivity : AppCompatActivity() {

    private lateinit var rootLayout: android.view.View
    private lateinit var cardLayout: android.view.View
    private lateinit var titleTextView: TextView
    private lateinit var openMapButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station)

        rootLayout = findViewById(R.id.gas_station_root)
        cardLayout = findViewById(R.id.gas_station_card)
        titleTextView = findViewById(R.id.tvGasStationTitle)
        openMapButton = findViewById(R.id.btn_open_map)

        applyThemeToViews()

        openMapButton.setOnClickListener {
            openMap()
        }
    }

    override fun onResume() {
        super.onResume()
        applyThemeToViews()
    }

    private fun openMap() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=petrol+station")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)
        ThemeManager.applyCardBackground(cardLayout)
        ThemeManager.applyPrimaryText(titleTextView)
        ThemeManager.applySecondaryText(openMapButton)
    }
}
