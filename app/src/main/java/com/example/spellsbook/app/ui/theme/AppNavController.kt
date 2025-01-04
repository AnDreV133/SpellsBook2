package com.example.spellsbook.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

internal val LocalAppNavController = staticCompositionLocalOf<NavHostController> {
    error("No navigation controller provided")
}

val appNavController: NavHostController
    @Composable
    get() = LocalAppNavController.current