package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.fragments.MainMenuBar
import com.example.spellsbook.app.ui.compose.fragments.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.app.ui.compose.screen.spells.holder.FilterMap
import com.example.spellsbook.app.ui.compose.screen.spells.holder.SpellsHeaderWithHeader
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import com.example.spellsbook.domain.usecase.IsPaidUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    }

    private val _state = MutableStateFlow(State())
    val state = _state
        .onStart { initState() }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        State()
    )

    private suspend fun initState() {
        _state.value = _state.value.copy(
            isPaidUser = isPaidUserUseCase.execute()
        )
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateListByFilterAndSorter -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        spells = getSpellsWithFilterAndSorterUseCase.execute(
                            event.filter,
                            event.sorter,
                            event.searchQuery
                        )
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
        SpellsHeaderWithHeader { spells, modifier ->
            println("spells -> " + spells.size )
            SpellList(
                spells = spells,
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
    spells: List<SpellShortModel>,
    navigateToDetail: (uuid: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(AppTheme.colors.firstBackgroundColor)
            .fillMaxSize(),
    ) {
        items(spells) { spell ->
            SpellListItem(
                spell = spell,
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) { navigateToDetail(spell.uuid) }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
private fun AllSpellsScreenPreview() {
    AppTheme {
        SpellList(
            spells = mutableListOf<SpellShortModel>().apply {
                repeat(20) {
                    add(
                        SpellShortModel(
                            uuid = "",
                            name = "Заклинание $it",
                            level = LevelEnum.LEVEL_1
                        )
                    )
                }
            },
            navigateToDetail = {}
        )
    }
}