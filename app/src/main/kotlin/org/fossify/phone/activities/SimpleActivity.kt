package org.fossify.phone.activities

import android.os.Bundle
import org.fossify.commons.activities.BaseSimpleActivity
import org.fossify.commons.extensions.baseConfig
import org.fossify.phone.R
import org.fossify.phone.extensions.config

open class SimpleActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyPulseDialDefaultThemeIfNeeded()
        super.onCreate(savedInstanceState)
    }

    private fun applyPulseDialDefaultThemeIfNeeded() {
        if (!config.isPulseThemeInitialized) {
            baseConfig.isSystemThemeEnabled = false
            baseConfig.isGlobalThemeEnabled = false
            baseConfig.backgroundColor = getColor(R.color.pulse_bg_deep)
            baseConfig.textColor = getColor(R.color.theme_dark_text_color)
            baseConfig.primaryColor = getColor(R.color.pulse_teal)
            baseConfig.accentColor = getColor(R.color.pulse_teal_bright)
            baseConfig.appIconColor = getColor(org.fossify.commons.R.color.md_teal_700)
            config.isPulseThemeInitialized = true
        }
    }

    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher_red,
        R.mipmap.ic_launcher_pink,
        R.mipmap.ic_launcher_purple,
        R.mipmap.ic_launcher_deep_purple,
        R.mipmap.ic_launcher_indigo,
        R.mipmap.ic_launcher_blue,
        R.mipmap.ic_launcher_light_blue,
        R.mipmap.ic_launcher_cyan,
        R.mipmap.ic_launcher_teal,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher_light_green,
        R.mipmap.ic_launcher_lime,
        R.mipmap.ic_launcher_yellow,
        R.mipmap.ic_launcher_amber,
        R.mipmap.ic_launcher_orange,
        R.mipmap.ic_launcher_deep_orange,
        R.mipmap.ic_launcher_brown,
        R.mipmap.ic_launcher_blue_grey,
        R.mipmap.ic_launcher_grey_black
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)

    override fun getRepositoryName() = "PulseDial"
}
