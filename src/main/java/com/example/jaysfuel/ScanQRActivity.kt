package com.example.jaysfuel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.jaysfuel.ui.scan.ScanQrScreen
import com.example.jaysfuel.ui.theme.JaysFuelTheme

/**
 * Activity that hosts the Compose scan UI.
 */
class ScanQRActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkTheme = isSystemInDarkTheme()

            JaysFuelTheme(darkTheme = darkTheme) {
                ScanQrScreen(
                    onClose = { finish() }
                )
            }
        }
    }
}
