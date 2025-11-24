package com.example.jaysfuel.theme

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat

/**
 * Global theme manager used by all screens.
 *
 * Day mode:
 *  - light background
 *  - white cards
 *  - dark text
 *
 * Night mode:
 *  - black background
 *  - deep purple cards
 *  - light text
 *
 * Important numbers (prices, points, etc.) use a yellow accent color.
 */
object ThemeManager {

    /** True for night mode, false for day mode. */
    var isNightMode: Boolean = false
        private set

    // Screen background colors
    private val dayBackgroundColor = Color.parseColor("#F2F2F7")
    private val nightBackgroundColor = Color.parseColor("#000000")

    // Card background colors
    private val dayCardColor = Color.parseColor("#FFFFFF")
    // Deep purple for night mode cards
    private val nightCardColor = Color.parseColor("#221738")

    // Text colors
    private val dayPrimaryTextColor = Color.parseColor("#121212")   // near black
    private val nightPrimaryTextColor = Color.parseColor("#FFFFFF") // pure white

    private val daySecondaryTextColor = Color.parseColor("#5F6368") // dark grey
    private val nightSecondaryTextColor = Color.parseColor("#B0B3B8") // light grey

    // Accent color for important numbers (prices, points, etc.)
    private val dayAccentTextColor = Color.parseColor("#F9A825")   // warm yellow
    private val nightAccentTextColor = Color.parseColor("#FFEB3B") // bright yellow

    /** Update the mode from sensor or other places. */
    fun updateMode(isNight: Boolean) {
        isNightMode = isNight
    }

    /** Toggle between day and night mode manually (button). */
    fun toggleMode() {
        isNightMode = !isNightMode
    }

    /** Apply background color to the root view of a screen. */
    fun applyBackground(root: View) {
        val color = if (isNightMode) nightBackgroundColor else dayBackgroundColor
        root.setBackgroundColor(color)
    }

    /**
     * Apply card background color to one or more card views.
     * The card views should use a shape (e.g. rounded_card.xml) as background;
     * we only tint the shape color.
     */
    fun applyCardBackground(vararg cardViews: View) {
        val color = if (isNightMode) nightCardColor else dayCardColor
        val tint = ColorStateList.valueOf(color)
        cardViews.forEach { card ->
            ViewCompat.setBackgroundTintList(card, tint)
        }
    }

    /** Apply primary text color (highest contrast). */
    fun applyPrimaryText(vararg textViews: TextView) {
        val color = if (isNightMode) nightPrimaryTextColor else dayPrimaryTextColor
        textViews.forEach { it.setTextColor(color) }
    }

    /** Apply secondary text color (less important information). */
    fun applySecondaryText(vararg textViews: TextView) {
        val color = if (isNightMode) nightSecondaryTextColor else daySecondaryTextColor
        textViews.forEach { it.setTextColor(color) }
    }

    /** Apply accent color to important values such as prices or points. */
    fun applyAccentText(vararg textViews: TextView) {
        val color = if (isNightMode) nightAccentTextColor else dayAccentTextColor
        textViews.forEach { it.setTextColor(color) }
    }
}
