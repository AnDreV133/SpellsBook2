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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@Composable
fun SpellsScreen(
    navController: NavController,
    content: @Composable (FilterMap, SortOptionEnum) -> Unit,
) {
    var filter by remember { mutableStateOf<FilterMap>(emptyMap()) }
    val sorter by remember { mutableStateOf(SortOptionEnum.BY_NAME) }

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
            callbackApplyFilter = { filter = it },
            modifier = wrapContentModifier,
        )
        content(filter, sorter)
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


