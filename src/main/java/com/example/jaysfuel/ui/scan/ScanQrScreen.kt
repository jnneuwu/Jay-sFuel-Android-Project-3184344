package com.example.jaysfuel.ui.scan

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.roundToInt

/**
 * Fake scan UI that looks like a scanner:
 * - Dark background
 * - Center frame with moving horizontal line
 * - Close button
 * Real camera integration can be added later.
 */
@Composable
fun ScanQrScreen(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {}
) {
    val frameSize = 260.dp
    val density = LocalDensity.current

    val infiniteTransition = rememberInfiniteTransition(label = "scan-line")
    val lineOffsetFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lineOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text(
                text = "Scan QR code",
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Align the QR code inside the frame.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Scan frame
        Box(
            modifier = Modifier
                .size(frameSize)
                .align(Alignment.Center)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            // Moving horizontal line
            val maxOffsetPx = with(density) { (frameSize - 4.dp).toPx() }
            val offsetY = (lineOffsetFraction * maxOffsetPx).roundToInt()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0xFF00FF80))
                    .offset { IntOffset(x = 0, y = offsetY) }
            )
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text("Close scanner")
        }
    }
}
