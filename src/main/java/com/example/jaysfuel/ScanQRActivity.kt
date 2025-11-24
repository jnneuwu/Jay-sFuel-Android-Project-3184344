package com.example.jaysfuel

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jaysfuel.theme.ThemeManager

/**
 * Placeholder QR scan screen using ThemeManager for colors.
 */
class ScanQRActivity : AppCompatActivity() {

    private lateinit var rootLayout: android.view.View
    private lateinit var cardLayout: android.view.View
    private lateinit var titleTextView: TextView
    private lateinit var hintTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        rootLayout = findViewById(R.id.scan_qr_root)
        cardLayout = findViewById(R.id.scan_card)
        titleTextView = findViewById(R.id.tvScanTitle)
        hintTextView = findViewById(R.id.tvScanHint)

        applyThemeToViews()
    }

    override fun onResume() {
        super.onResume()
        applyThemeToViews()
    }

    private fun applyThemeToViews() {
        ThemeManager.applyBackground(rootLayout)
        ThemeManager.applyCardBackground(cardLayout)
        ThemeManager.applyPrimaryText(titleTextView)
        ThemeManager.applySecondaryText(hintTextView)
    }
}
