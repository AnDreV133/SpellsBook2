package com.example.spellsbook.app.ui.compose.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder


sealed class NavEndpoint : Route, Destination {
    object Books : NavEndpoint() {
        override val route = "books"
        override val destination = route
    }

    object Spells : NavEndpoint() {
        override val route = "spells"
        override val destination = route
    }
    object Settings : NavEndpoint() {
        override val route = "settings"
        override val destination = route
    }

//    data object Prepared : NavEndpoint("prepared")
//    data object KnownSpells : NavEndpoint("known")
//    data object SpellsWithAddInBook : NavEndpoint("spells_with_add")


    class UnknownSpells(id: Long? = null) : NavEndpoint() {
        override val route = "books/{id}/unknown"
        override val destination: String = "books/$id/unknown"
    }

    class KnownSpells(id: Long? = null) : NavEndpoint() {
        override val route = "books/{id}/known"
        override val destination: String = "books/$id/known"
    }

    object AuthorSpells : NavEndpoint() {
        override val route = "spells/author"
        override val destination: String = route
    }

    class SpellByUuid(uuid: String? = null) : NavEndpoint() {
        override val route = "spells/{uuid}"
        override val destination: String = "spells/$uuid"
    }

    class SpellByUuidWithModifying(uuid: String? = null) : NavEndpoint() {
        override val route = "spells/{uuid}/modify"
        override val destination: String = "spells/$uuid/modify"
    }

    override fun equals(other: Any?): Boolean {
        return this.route == (other as Route).route
    }

    override fun hashCode(): Int {
        return route.hashCode()
    }
}

interface Destination {
    val destination: String
}

interface Route {
    val route: String
}

fun NavController.navigate(endpoint: Destination) {
    navigate(endpoint.destination)
}

fun NavController.navigate(endpoint: Destination, builder: NavOptionsBuilder.() -> Unit) {
    navigate(endpoint.destination, builder)
}