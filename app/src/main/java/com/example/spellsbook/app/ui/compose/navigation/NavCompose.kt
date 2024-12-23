package com.example.spellsbook.app.ui.compose.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spellsbook.app.ui.compose.fragments.BookMenuBar
import com.example.spellsbook.app.ui.compose.fragments.MainMenuBar
import com.example.spellsbook.app.ui.compose.fragments.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.screen.AuthorSpellListScreen
import com.example.spellsbook.app.ui.compose.screen.BooksScreen
import com.example.spellsbook.app.ui.compose.screen.spells.KnownSpellsScreen
import com.example.spellsbook.app.ui.compose.screen.SettingsScreen
import com.example.spellsbook.app.ui.compose.screen.SpellDetailScreen
import com.example.spellsbook.app.ui.compose.screen.spells.AllSpellsScreen
import com.example.spellsbook.app.ui.compose.screen.spells.SpellsByBookScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavEndpoint.Books.route
    ) {
        composable(route = NavEndpoint.Books.route) {
            ScreenWithMenuBar(
                menuBar = { MainMenuBar(navController, NavEndpoint.Books) }
            ) {
                BooksScreen(navController = navController)
            }
        }

        composable(route = NavEndpoint.Settings.route) {
            ScreenWithMenuBar(
                menuBar = { MainMenuBar(navController, NavEndpoint.Settings) }
            ) {
                SettingsScreen()
            }
        }

        composable(route = NavEndpoint.Spells.route) {
            AllSpellsScreen(navController = navController)
        }

        composable(
            route = NavEndpoint.UnknownSpells().route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            val bookId = it.arguments?.getLong("id") ?: return@composable

            ScreenWithMenuBar(
                menuBar = {
                    BookMenuBar(
                        bookId = bookId,
                        navController = navController,
                        changedEndpoint = NavEndpoint.UnknownSpells(bookId)
                    )
                }
            ) {
                SpellsByBookScreen(
                    bookId = bookId,
                    navController = navController
                )
            }
        }

        composable(
            route = NavEndpoint.KnownSpells().route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            val bookId = it.arguments?.getLong("id") ?: return@composable

            ScreenWithMenuBar(
                menuBar = {
                    BookMenuBar(
                        bookId = bookId,
                        navController = navController,
                        changedEndpoint = NavEndpoint.KnownSpells(bookId)
                    )
                }
            ) {
                KnownSpellsScreen(
                    bookId = bookId,
                    navController = navController
                )
            }
        }

        composable(
            route = NavEndpoint.AuthorSpells.route,
        ) {
            AuthorSpellListScreen(
                navController = navController
            )
        }

        composable(
            route = NavEndpoint.SpellByUuid().route,
            arguments = listOf(
                navArgument("uuid") {
                    type = NavType.StringType
                }
            )
        ) {
            it.arguments?.getString("uuid").let { uuid ->
                if (uuid != null)
                    SpellDetailScreen(spellUuid = uuid)
            }
        }

        composable(
            route = NavEndpoint.SpellsByUuidWithModifying().route,
            arguments = listOf(
                navArgument("uuid") {
                    type = NavType.StringType
                }
            )
        ) {
            it.arguments?.getString("uuid").let { uuid ->
                if (uuid != null)
                    Text("stub")
            }
        }
    }
}


