package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllSpellListViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
) : ViewModel() {
    data class State(
        val spells: List<SpellShortModel> = emptyList(),
//        val filter: FilterMap = emptyMap(),
//        val sorter: SortOptionEnum = SortOptionEnum.BY_NAME
    )

    sealed class Event {
        class UpdateListByFilterAndSorter(
            val filter: FilterMap,
            val sorter: SortOptionEnum
        ) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateListByFilterAndSorter -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        spells = getSpellsWithFilterAndSorterUseCase.execute(
                            event.filter,
                            event.sorter
                        ),
//                        filter = event.filter,
//                        sorter = event.sorter
                    )
                }
            }
        }
    }
}

@Composable
fun AllSpellsScreen(
    navController: NavController,
    viewModel: AllSpellListViewModel = hiltViewModel(),
) {
    SpellsScreenHolder { filter, sorter ->
        SpellList(
            filter = filter,
            sorter = sorter,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SpellList(
    filter: FilterMap,
    sorter: SortOptionEnum,
    navController: NavController,
    viewModel: AllSpellListViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    viewModel.onEvent(
        AllSpellListViewModel
            .Event
            .UpdateListByFilterAndSorter(
                filter = filter,
                sorter = sorter
            )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.spells) { spell ->
            SpellListItem(
                spell = spell,
            ) {
                navController.navigate(
                    NavEndpoint
                        .SpellByUuid(spell.uuid)
                )
            }
        }
    }
}

