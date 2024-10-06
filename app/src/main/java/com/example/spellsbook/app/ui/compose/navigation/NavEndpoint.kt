package com.example.spellsbook.app.ui.compose.navigation

import androidx.navigation.NavHostController

sealed class NavEndpoint(val route: String) {
    data object Books : NavEndpoint("books")
    data object Spells : NavEndpoint("spells")
    data object Settings : NavEndpoint("settings")

    data object Prepared : NavEndpoint("prepared")
    data object Known : NavEndpoint("known")
    data object SpellsWithAdd : NavEndpoint("spells_with_add")

    class BookById(id: Long) : NavEndpoint("books/$id")
    class SpellByUuid(uuid: String) : NavEndpoint("spells/$uuid")
}

fun NavHostController.navigate(endpoint: NavEndpoint) {
    navigate(endpoint.route)
}