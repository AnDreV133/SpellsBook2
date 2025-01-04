package com.example.spellsbook.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat

data class AppColors(
    val firstBackgroundColor: Color,
    val secondBackgroundColor: Color,
    val firstForegroundColor: Color,
)

data class AppTextStiles(
    val primaryTextStyle: TextStyle,
    val primaryBoldTextStyle: TextStyle,
    val tintTextStyle: TextStyle
)

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}

private val LocalTextStyles = staticCompositionLocalOf<AppTextStiles> {
    error("No text styles provided")
}

private val LightAppColorScheme = AppColors(
    firstBackgroundColor = DarkGrayColor,
    secondBackgroundColor = CreamColor,
    firstForegroundColor = DarkCreamColor
)

private val AppTextStileScheme = AppTextStiles(
    primaryTextStyle = BaseBoldTextStyle,
    primaryBoldTextStyle = BaseBoldTextStyle,
    tintTextStyle = TintTextStyle
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightAppColorScheme
    val textStyleScheme = AppTextStileScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.firstBackgroundColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppColors provides colorScheme,
        LocalTextStyles provides textStyleScheme,
        content = content
    )
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val textStyles: AppTextStiles
        @Composable
        get() = LocalTextStyles.current
}