package com.example.spellsbook.app.ui.compose.screen.spell_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint


@Composable
fun SpellsScreen(
    navController: NavController,
    bookId: Long? = null,
    viewModel: SpellListViewModel = hiltViewModel()
) {
    val wrapContentModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ) {
        SearchRow()
        SortRow()
        FilterGrid(
            viewModel = viewModel,
            modifier = wrapContentModifier,
        )
        SpellsList(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SearchRow(
    modifier: Modifier = Modifier
) {

}


@Composable
private fun SortRow() {
}

@Composable
fun SpellsList(
    navController: NavController,
    viewModel: SpellListViewModel,
    bookId: Long? = null
) {
    val state = viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.spells) { spell ->
            SpellListItem(
                navigate = {
                    navController.navigate(
                        NavEndpoint
                            .SpellByUuid
                            .getDestination(spell.uuid)
                    )
                },
                spell = spell,
                bookId = bookId
            )
        }
    }
}

