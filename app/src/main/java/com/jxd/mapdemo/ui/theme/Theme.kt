package com.jxd.mapdemo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun MapDemoTheme(themeViewModel: ThemeViewModel, content: @Composable () -> Unit) {
    val currentTheme by themeViewModel.currentTheme.collectAsState(initial = ThemeModel.AUTO)
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colors = when (currentTheme) {
        ThemeModel.AUTO -> {
            if (darkTheme) {
                DarkColorPalette
            } else {
                LightColorPalette
            }
        }
        ThemeModel.LIGHT -> LightColorPalette
        ThemeModel.DARK -> DarkColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}