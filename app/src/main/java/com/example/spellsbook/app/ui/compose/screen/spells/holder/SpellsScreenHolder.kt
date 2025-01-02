package com.example.spellsbook.app.ui.compose.screen.spells.holder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.spellsbook.domain.enums.SortOptionEnum


@Composable
fun SpellsScreenHolder(
    content: @Composable (
        filter: FilterMap,
        sorter: SortOptionEnum,
        searchQuery: String,
        modifier: Modifier,
    ) -> Unit,
) {
    var filter by remember { mutableStateOf<FilterMap>(emptyMap()) }
    val sorter by remember { mutableStateOf(SortOptionEnum.BY_NAME) }
    var searchQuery by remember { mutableStateOf("") }
    val wrapContentModifier = remember {
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp, 0.dp)
    }
    var isHeadVisible by remember { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                isHeadVisible = available.y > -0.2

                return super.onPreScroll(available, source)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ) {
        AnimatedVisibility(
            visible = isHeadVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 1000)
            ) + expandVertically(),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 1000)
            ) + shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
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
            }
        }
        content(
            filter,
            sorter,
            searchQuery,
            Modifier.nestedScroll(nestedScrollConnection)
        )
    }
}


@Composable
private fun SortRow() {
}


