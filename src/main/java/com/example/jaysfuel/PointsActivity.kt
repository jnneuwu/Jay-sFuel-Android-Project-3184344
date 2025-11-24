// PointsActivity.kt
package com.example.jaysfuel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jaysfuel.theme.ThemeManager

/**
 * Legacy points activity.
 * Kept as a safe placeholder if started from anywhere.
 */
class PointsActivity : AppCompatActivity() {

    private lateinit var rootLayout: android.view.View
    private lateinit var cardLayout: android.view.View
    private lateinit var infoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)

        rootLayout = findViewById(R.id.activity_points_root)
        cardLayout = findViewById(R.id.points_activity_card)
        infoTextView = findViewById(R.id.tvPointsActivityInfo)

        applyThemeToViews()
    }

    override fun onResume() {
        super.onResume()
        applyThemeToViews()
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)
        ThemeManager.applyCardBackground(cardLayout)
        ThemeManager.applyPrimaryText(infoTextView)
    }
}
