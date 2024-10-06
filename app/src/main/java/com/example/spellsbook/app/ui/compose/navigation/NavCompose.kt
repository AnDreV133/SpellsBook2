package com.example.spellsbook.app.ui.compose.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spellsbook.app.ui.compose.BookByIdScreen
import com.example.spellsbook.app.ui.compose.BooksCompose
import com.example.spellsbook.app.ui.compose.MainMenuBar
import com.example.spellsbook.app.ui.compose.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.spells.SpellsCompose

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
                BooksCompose(navController).BooksScreen()
            }
        }

        composable(
            route = "${NavEndpoint.Books.route}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            ScreenWithMenuBar(
                menuBar = { Text("stub") }
            ) {
                it.arguments?.getLong("id").let { id ->
                    if (id != null)
                        BookByIdScreen(
                            id = id,
                            navController = navController
                        )
                }
            }
        }

        composable(route = NavEndpoint.Settings.route) {
            ScreenWithMenuBar(
                menuBar = { MainMenuBar(navController, NavEndpoint.Settings) }
            ) {
                Text("Settings screen")
            }
        }

        composable(route = NavEndpoint.Spells.route) {
            ScreenWithMenuBar(
                menuBar = { MainMenuBar(navController, NavEndpoint.Spells) }
            ) {
                SpellsCompose(navController = navController).Screen()
            }
        }
    }
}