package com.example.spellsbook.app.ui.compose.navigation

import androidx.navigation.NavHostController

sealed class NavEndpoint(val route: String) {
    object Books : NavEndpoint("books")
    object Spells : NavEndpoint("spells")
    object Settings : NavEndpoint("settings")

//    data object Prepared : NavEndpoint("prepared")
//    data object KnownSpells : NavEndpoint("known")
//    data object SpellsWithAddInBook : NavEndpoint("spells_with_add")


    object UnknownSpells : NavEndpoint("books/{id}/unknown"), DestinationById<Long> {
        override fun getDestination(id: Long): String = "books/${id}/unknown"
    }

    object KnownSpells : NavEndpoint("books/{id}/known"), DestinationById<Long> {
        override fun getDestination(id: Long): String = "books/${id}/known"
    }

    object SpellByUuid : NavEndpoint("spells/{uuid}/known"), DestinationById<String> {
        override fun getDestination(id: String): String = "spells/${id}/known"
    }
}

interface DestinationById<T : Any> {
    fun getDestination(id: T): String
}

//fun NavHostController.navigate(endpoint: DestinationById<Any>) {
//    navigate(endpoint.getDestination())
//}