package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.AddSpellToBookUseCase
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterByBookIdUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellFromBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpellsByBookViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterByBookIdUseCase: GetSpellsWithFilterAndSorterByBookIdUseCase,
    private val addSpellToBookUseCase: AddSpellToBookUseCase,
    private val removeSpellFromBookUseCase: RemoveSpellFromBookUseCase
) : ViewModel() {
    data class State(
        val spells: Map<SpellShortModel, Boolean> = emptyMap(),
    )

    sealed class Event {
        class ExecuteListByFilterAndSorter(
            val bookId: Long,
            val filter: FilterMap,
            val sorter: SortOptionEnum
        ) : Event()

        class AddToBook(
            val bookId: Long,
            val spell: SpellShortModel
        ) : Event()

        class RemoveFromBook(
            val bookId: Long,
            val spell: SpellShortModel
        ) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.ExecuteListByFilterAndSorter -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        spells = getSpellsWithFilterAndSorterByBookIdUseCase.execute(
                            bookId = event.bookId,
                            filter = event.filter,
                            sorter = event.sorter
                        ).associate { it },
                    )
                }
            }

            is Event.AddToBook -> {
                viewModelScope.launch {
                    launch {
                        addSpellToBookUseCase.execute(
                            event.bookId,
                            event.spell.uuid
                        )
                    }
                    launch {
                        _state.value = _state.value.copy(
                            spells = _state.value.spells
                                .toMutableMap()
                                .apply {
                                    compute(event.spell) { _, _ -> true }
                                }
                        )
                    }
                }
            }

            is Event.RemoveFromBook -> {
                viewModelScope.launch {
                    launch {
                        removeSpellFromBookUseCase.execute(
                            event.bookId,
                            event.spell.uuid
                        )
                    }
                    launch {
                        _state.value = _state.value.copy(
                            spells = _state.value.spells
                                .toMutableMap()
                                .apply {
                                    compute(event.spell) { _, _ -> false }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpellsByBookScreen(
    bookId: Long,
    navController: NavController,
    viewModel: SpellsByBookViewModel = hiltViewModel(),
) {
    SpellsScreenHolder { filter, sorter ->
        SpellList(
            bookId = bookId,
            filter = filter,
            sorter = sorter,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SpellList(
    bookId: Long,
    filter: FilterMap,
    sorter: SortOptionEnum,
    navController: NavController,
    viewModel: SpellsByBookViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    val navigateFunc = remember {
        { spellAndChanged: Pair<SpellShortModel, Boolean> ->
            navController.navigate(
                NavEndpoint
                    .SpellByUuid(spellAndChanged.first.uuid)
            )
        }
    }

    val addToBookFunc = remember {
        { spell: SpellShortModel ->
            viewModel.onEvent(
                SpellsByBookViewModel
                    .Event
                    .AddToBook(
                        bookId = bookId,
                        spell = spell
                    )
            )
        }
    }

    val removeFromBookFunc = remember {
        { spell: SpellShortModel ->
            viewModel.onEvent(
                SpellsByBookViewModel
                    .Event
                    .RemoveFromBook(
                        bookId = bookId,
                        spell = spell
                    )
            )
        }
    }

    viewModel.onEvent(
        SpellsByBookViewModel
            .Event
            .ExecuteListByFilterAndSorter(
                bookId = bookId,
                filter = filter,
                sorter = sorter
            )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.spells.toList()) { spellAndChanged ->
            SpellListItemWithSwitchButton(
                spellAndChanged = spellAndChanged,
                addToBook = addToBookFunc,
                removeFromBook = removeFromBookFunc,
            ) { navigateFunc(spellAndChanged) }
        }
    }
}

