package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spellsbook.domain.enums.SortOptionEnum


@Composable
fun SpellsScreenHolder(
    content: @Composable (filter: FilterMap, sorter: SortOptionEnum, searchQuery: String) -> Unit,
) {
    var filter by remember { mutableStateOf<FilterMap>(emptyMap()) }
    val sorter by remember { mutableStateOf(SortOptionEnum.BY_NAME) }
    var searchQuery by remember { mutableStateOf("") }

    val wrapContentModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(4.dp, 0.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ) {
        SearchRow(
            modifier = wrapContentModifier
                .padding(top = 4.dp, bottom = 4.dp),
            callbackSearchQuery = { searchQuery = it },
        )
        SortRow()
        FilterRow(
            modifier = wrapContentModifier,
            callbackApplyFilter = { filter = it },
        )
        content(filter, sorter, searchQuery)
    }
}


@Composable
private fun SortRow() {
}


