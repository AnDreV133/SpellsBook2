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
import com.example.spellsbook.app.ui.compose.screen.BookByIdScreen
import com.example.spellsbook.app.ui.compose.screen.BooksScreen
import com.example.spellsbook.app.ui.compose.screen.SettingsScreen
import com.example.spellsbook.app.ui.compose.screen.SpellDetailScreen
import com.example.spellsbook.app.ui.compose.screen.SpellsScreen

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
            ScreenWithMenuBar(
                menuBar = { MainMenuBar(navController, NavEndpoint.Spells) }
            ) {
                SpellsScreen(navController = navController)
            }
        }

        composable(
            route = NavEndpoint.KnownSpells.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            ScreenWithMenuBar(
                menuBar = {
                    BookMenuBar(
                        navController = navController,
                        changedEndpoint = NavEndpoint.KnownSpells
                    )
                }
            ) {
                it.arguments?.getLong("id").let { id ->
                    SpellsScreen(
                        bookId = id,
                        navController = navController
                    )
                }
            }
        }

        composable(
            route = NavEndpoint.UnknownSpells.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            ScreenWithMenuBar(
                menuBar = {
                    BookMenuBar(
                        navController = navController,
                        changedEndpoint = NavEndpoint.UnknownSpells
                    )
                }
            ) {
                Text(text = "stub")
//                it.arguments?.getLong("id").let { id ->
//                    if (id != null)
//                        BookByIdScreen(
//                            id = id,
//                            navController = navController
//                        )
//                }
            }
        }

        composable(
            route = NavEndpoint.SpellByUuid.route,
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
    }
}