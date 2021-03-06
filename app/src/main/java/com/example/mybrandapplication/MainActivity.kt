package com.example.mybrandapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import wang.relish.colorpicker.ColorPickerDialog

class MainActivity : AppCompatActivity() {
    @ColorInt
    private var currentColor = INITIAL_COLOR
    private lateinit var argbColorPreview: FrameLayout
    private lateinit var argbLabel: TextView
    private lateinit var argbAlphaComponentLabel: TextView
    private lateinit var argbRedComponentLabel: TextView
    private lateinit var argbGreenComponentLabel: TextView
    private lateinit var argbBlueComponentLabel: TextView
    private lateinit var recreateActivityButton: Button
    private lateinit var brandThemeOverlayEnabledSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentColor = getColorPrimaryFromCurrentContextTheme(this)

        argbColorPreview = findViewById(R.id.argb_color_preview)
        argbColorPreview.setBackgroundColor(currentColor)

        argbLabel = findViewById(R.id.argb_label)
        argbAlphaComponentLabel = findViewById(R.id.argb_alpha_component_label)
        argbRedComponentLabel = findViewById(R.id.argb_red_component_label)
        argbGreenComponentLabel = findViewById(R.id.argb_green_component_label)
        argbBlueComponentLabel = findViewById(R.id.argb_blue_component_label)
        updateArgbLabels(currentColor)

        recreateActivityButton = findViewById(R.id.recreate_activity_button)
        recreateActivityButton.setOnClickListener {
            this.recreate()
        }

        brandThemeOverlayEnabledSwitch = findViewById(R.id.brand_theme_overlay_enabled_switch)
        brandThemeOverlayEnabledSwitch.isChecked = isBrandThemeOverlayEnabled(this)
        brandThemeOverlayEnabledSwitch.setOnCheckedChangeListener { _, isChecked ->
            setBrandThemeOverlayEnabled(this@MainActivity, isChecked)
        }

        argbColorPreview.setOnClickListener {
            ColorPickerDialog.Builder(this, currentColor).setOnColorPickedListener { color ->
                currentColor = color
                updateArgbColorPreview(currentColor)
                updateArgbLabels(currentColor)
                setBrandColorInSharedPreferences(this, currentColor)
            }.build().show()
        }
    }

    private fun updateArgbColorPreview(@ColorInt color: Int) {
        argbColorPreview.setBackgroundColor(color)
    }

    private fun updateArgbLabels(@ColorInt color: Int) {
        val alphaComponentHexString = String.format("%02X", color.alpha)
        val redComponentHexString = String.format("%02X", color.red)
        val greenComponentHexString = String.format("%02X", color.green)
        val blueComponentHexString = String.format("%02X", color.blue)
        val argbHexString = "#${color.toArgbHexString()}"

        argbLabel.text = argbHexString
        argbAlphaComponentLabel.text =
            resources.getString(R.string.rgba_alpha, alphaComponentHexString)
        argbRedComponentLabel.text = resources.getString(R.string.rgba_red, redComponentHexString)
        argbGreenComponentLabel.text =
            resources.getString(R.string.rgba_green, greenComponentHexString)
        argbBlueComponentLabel.text =
            resources.getString(R.string.rgba_blue, blueComponentHexString)
    }


    // 2nd answer in https://stackoverflow.com/questions/27611173/how-to-get-accent-color-programmatically
    @ColorInt
    private fun getColorPrimaryFromCurrentContextTheme(context: Context): Int {
        val outTypedValue = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, outTypedValue, true)
        return outTypedValue.data
    }

    companion object {
        @ColorInt
        const val INITIAL_COLOR = Color.BLACK
    }
}

