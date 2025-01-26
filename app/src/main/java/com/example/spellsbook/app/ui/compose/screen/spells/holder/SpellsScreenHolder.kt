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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class SpellsHeaderViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
) : ViewModel() {
    data class State(
        val spells: List<SpellShortModel> = emptyList(),
        val filter: FilterMap = emptyMap(),
        val sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
        val searchQuery: String = "",
    )

//    var spells by mutableStateOf<List<SpellShortModel>>(emptyList())
//    var isPaidUser by mutableStateOf(false)
//        private set
//    var filter by mutableStateOf<FilterMap>(emptyMap())
//    var sorter by mutableStateOf(SortOptionEnum.BY_NAME)
//    var searchQuery by mutableStateOf("")

    private val _state = MutableStateFlow(State())
    val state = _state
        .map {
            it.copy(
                spells = getSpellsWithFilterAndSorterUseCase.execute(
                    filter = it.filter,
                    sorter = it.sorter,
                    searchQuery = it.searchQuery
                )
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State()
        )

    fun setFilter(filter: FilterMap) {
        viewModelScope.launch {
            _state.value = _state.value.copy(filter = filter)
        }
    }

    fun setSorter(sorter: SortOptionEnum) {
        viewModelScope.launch {
            _state.value = _state.value.copy(sorter = sorter)
        }
    }

    fun setSearchQuery(searchQuery: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(searchQuery = searchQuery)
        }
    }
}

@Composable
fun SpellsHeaderWithHeader(
    viewModel: SpellsHeaderViewModel = hiltViewModel(),
    content: @Composable (
        spells: List<SpellShortModel>,
        modifier: Modifier,
    ) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    val wrapContentModifier = remember {
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp, 0.dp)
    }
    var isHeadVisible by remember { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            private val dead_zone = 8
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (abs(available.y) > dead_zone)
                    isHeadVisible = available.y > -dead_zone
                println(available.y)
                return super.onPreScroll(available, source)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = AppTheme.colors.firstBackgroundColor
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
                    .padding(bottom = 4.dp)
                    .zIndex(20.0f)
            ) {
                var query by rememberSaveable { mutableStateOf("") }
                SearchRow(
                    modifier = wrapContentModifier
                        .padding(top = 4.dp, bottom = 4.dp),
                    query = query,
                    onValueChange = { query = it },
                    onClickSearch = { viewModel.setSearchQuery(query) },
                )
                SortRow()
                FilterRow(
                    modifier = wrapContentModifier,
                    callbackApplyFilter = viewModel::setFilter,
                )
            }
        }
        content(
            state.spells,
            Modifier.nestedScroll(nestedScrollConnection)
        )
    }

}

@Composable
private fun SortRow() {
}


