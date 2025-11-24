package com.example.jaysfuel.ui.coupon

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.jaysfuel.model.UserManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

/**
 * Screen that shows a QR code for the selected coupon.
 * Staff can scan this code to validate the reward.
 */
@Composable
fun CouponQrScreen(
    onBack: () -> Unit
) {
    val coupon = UserManager.currentCoupon

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Coupon QR code",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (coupon == null) {
            Text("No coupon selected.")
        } else {
            Text(
                text = coupon.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Show this code to the staff to redeem your reward.",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            val qrBitmapState = remember(coupon.id) {
                mutableStateOf(
                    createQrBitmap("coupon:${coupon.id}:${coupon.name}")
                )
            }

            val qrBitmap = qrBitmapState.value

            Card {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (qrBitmap != null) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "Coupon QR code"
                        )
                    } else {
                        Text("QR code could not be generated")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onBack) {
            Text("BACK")
        }
    }
}

/**
 * Generates a QR code bitmap from the given text using ZXing.
 */
fun createQrBitmap(content: String, size: Int = 512): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] =
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    } catch (_: Exception) {
        null
    }
}
