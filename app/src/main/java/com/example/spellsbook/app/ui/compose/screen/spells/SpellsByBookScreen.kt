package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.app.ui.compose.fragments.BookMenuBar
import com.example.spellsbook.app.ui.compose.fragments.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.app.ui.compose.screen.spells.holder.SpellsHeaderWithHeader
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.appNavController
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.AddSpellToBookUseCase
import com.example.spellsbook.domain.usecase.MarkSpellsIfInBook
import com.example.spellsbook.domain.usecase.RemoveSpellFromBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpellsByBookViewModel @Inject constructor(
    private val markSpellsIfInBook: MarkSpellsIfInBook,
    private val addSpellToBookUseCase: AddSpellToBookUseCase,
    private val removeSpellFromBookUseCase: RemoveSpellFromBookUseCase
) : ViewModel() {
    data class State(
        val spells: Map<SpellShortModel, Boolean> = emptyMap(),
    )

    sealed class Event {
        class ChangeFilter(
            val bookId: Long,
            val spells: List<SpellShortModel>,
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
            is Event.ChangeFilter -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        spells = markSpellsIfInBook
                            .execute(
                                bookId = event.bookId,
                                spells = event.spells
                            )
                            .associate { it },
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
    viewModel: SpellsByBookViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = appNavController

    ScreenWithMenuBar(
        menuBar = {
            BookMenuBar(
                bookId = bookId,
                navController = navController,
                changedEndpoint = NavEndpoint.UnknownSpells(bookId)
            )
        }
    ) {
        SpellsHeaderWithHeader { spells, modifier ->
            println(spells.size)
            LaunchedEffect(spells) {
                viewModel.onEvent(
                    SpellsByBookViewModel
                        .Event
                        .ChangeFilter(
                            bookId,
                            spells
                        )
                )
            }

            SpellList(
                spellAndChangeList = state.spells.toList(),
                addToBook = {
                    viewModel.onEvent(
                        SpellsByBookViewModel
                            .Event
                            .AddToBook(
                                bookId = bookId,
                                spell = it
                            )
                    )
                },
                removeFromBook = {
                    viewModel.onEvent(
                        SpellsByBookViewModel
                            .Event
                            .RemoveFromBook(
                                bookId = bookId,
                                spell = it
                            )
                    )
                },
                modifier = modifier,
                navigateToDetail = { uuid ->
                    navController.navigate(NavEndpoint.SpellByUuid(uuid))
                }
            )
        }
    }
}

@Composable
private fun SpellList(
    spellAndChangeList: List<Pair<SpellShortModel, Boolean>>,
    addToBook: (model: SpellShortModel) -> Unit,
    removeFromBook: (model: SpellShortModel) -> Unit,
    navigateToDetail: (uuid: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(AppTheme.colors.firstBackgroundColor)
            .fillMaxSize(),
    ) {
        items(spellAndChangeList) { spellAndChange ->
            SpellListItemWithSwitchButton(
                spell = spellAndChange.first,
                changed = spellAndChange.second,
                navigateToDetail = {
                    navigateToDetail(spellAndChange.first.uuid)
                },
                addToBook = addToBook,
                removeFromBook = removeFromBook,
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            )
        }
    }
}