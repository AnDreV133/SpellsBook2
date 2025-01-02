package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.fragments.MainMenuBar
import com.example.spellsbook.app.ui.compose.fragments.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import com.example.spellsbook.domain.usecase.IsPaidUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllSpellListViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
    private val isPaidUserUseCase: IsPaidUserUseCase
) : ViewModel() {
    data class State(
        val spells: List<SpellShortModel> = emptyList(),
        val isPaidUser: Boolean = false,
//        val filter: FilterMap = emptyMap(),
//        val sorter: SortOptionEnum = SortOptionEnum.BY_NAME
    )

    sealed class Event {
        class UpdateListByFilterAndSorter(
            val filter: FilterMap,
            val sorter: SortOptionEnum,
            val searchQuery: String
        ) : Event()

        object CheckPaidUser : Event()
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
                            event.sorter,
                            event.searchQuery
                        ),
                    )
                }
            }

            is Event.CheckPaidUser -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        isPaidUser = isPaidUserUseCase.execute()
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
    val state by viewModel.state.collectAsState()

    ScreenWithMenuBar(
        menuBar = {
            MainMenuBar(
                navController = navController,
                changedEndpoint = NavEndpoint.Spells
            )
        },
        floatingButton = {
            if (state.isPaidUser)
                AddFloatingButton(
                    onClick = {
                        navController.navigate(NavEndpoint.AuthorSpells)
                    }
                )
        }
    ) {
        SpellsScreenHolder { filter, sorter, searchQuery ->
            SpellList(
                filter = filter,
                sorter = sorter,
                searchQuery = searchQuery,
                navController = navController,
                viewModel = viewModel
            )
        }
    }

    LaunchedEffect (Unit) {
        viewModel.onEvent(AllSpellListViewModel.Event.CheckPaidUser)
    }
}

@Composable
private fun SpellList(
    filter: FilterMap,
    sorter: SortOptionEnum,
    searchQuery: String,
    navController: NavController,
    viewModel: AllSpellListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    viewModel.onEvent(
        AllSpellListViewModel
            .Event
            .UpdateListByFilterAndSorter(
                filter = filter,
                sorter = sorter,
                searchQuery = searchQuery
            )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.spells) { spell ->
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

