package com.example.spellsbook.app.ui.compose.fragments

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint

data class MenuNavigationItem(
    val title: String,
    val icon: ImageVector,
    val navEndpoint: NavEndpoint
)

@Composable
fun MainMenuBar(
    navController: NavController,
    changedEndpoint: NavEndpoint,
) {
    MenuBar(
        navController = navController,
        changedEndpoint = changedEndpoint,
        items = itemsForMainMenu
    )
}

@Composable
fun BookMenuBar(
    bookId: Long,
    navController: NavController,
    changedEndpoint: NavEndpoint,
) {
    MenuBar(
        navController = navController,
        changedEndpoint = changedEndpoint,
        items = itemsForBookMenu(bookId)
    )
}

@Composable
private fun MenuBar(
    navController: NavController,
    changedEndpoint: NavEndpoint,
    items: List<MenuNavigationItem>
) {
    val selectedItemIndex = items.indexOfFirst { it.navEndpoint == changedEndpoint }
    if (selectedItemIndex < 0) throw IllegalArgumentException("Invalid endpoint $changedEndpoint in ${items.map { it.navEndpoint}}")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    navController.navigate(item.navEndpoint) {
                        this.restoreState = true
                        this.popUpTo(NavEndpoint.Books.route)
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}

private val itemsForMainMenu
    @Composable
    get() = listOf(
        MenuNavigationItem(
            title = stringResource(id = R.string.menu_books),
            icon = ImageVector.vectorResource(R.drawable.ic_block_48),
            navEndpoint = NavEndpoint.Books
        ),
        MenuNavigationItem(
            title = stringResource(id = R.string.menu_spells),
            icon = ImageVector.vectorResource(R.drawable.ic_block_48),
            navEndpoint = NavEndpoint.Spells
        ),
        MenuNavigationItem(
            title = stringResource(id = R.string.menu_settings),
            icon = ImageVector.vectorResource(R.drawable.ic_block_48),
            navEndpoint = NavEndpoint.Settings
        )
    )

@Composable
private fun itemsForBookMenu(bookId: Long) = listOf(
        MenuNavigationItem(
            title = stringResource(id = R.string.menu_spells),
            icon = ImageVector.vectorResource(R.drawable.ic_block_48),
            navEndpoint = NavEndpoint.UnknownSpells(bookId)
        ),
        MenuNavigationItem(
            title = stringResource(id = R.string.menu_known_spells),
            icon = ImageVector.vectorResource(R.drawable.ic_block_48),
            navEndpoint = NavEndpoint.KnownSpells(bookId)
        )
    )
