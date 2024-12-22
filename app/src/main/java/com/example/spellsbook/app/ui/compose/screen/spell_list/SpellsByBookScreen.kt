package com.example.spellsbook.app.ui.compose.screen.spell_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.enums.LevelEnum
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
        val spells: List<Pair<SpellShortModel, Boolean>> = emptyList(),
    )

    sealed class Event {
        class UpdateListByFilterAndSorter(
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
            is Event.UpdateListByFilterAndSorter -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        spells = getSpellsWithFilterAndSorterByBookIdUseCase.execute(
                            bookId = event.bookId,
                            filter = event.filter,
                            sorter = event.sorter
                        ),
                    )
                }
            }

            is Event.AddToBook -> {
                viewModelScope.launch {
                    addSpellToBookUseCase.execute(
                        event.bookId,
                        event.spell.uuid
                    )
                }
            }

            is Event.RemoveFromBook -> {
                viewModelScope.launch {
                    removeSpellFromBookUseCase.execute(
                        event.bookId,
                        event.spell.uuid
                    )
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

    val transferToBookFunc = remember {
        { spellAndChanged: Pair<SpellShortModel, Boolean> ->
            viewModel.onEvent(
                if (spellAndChanged.second)
                    SpellsByBookViewModel.Event.RemoveFromBook(
                        bookId = bookId,
                        spell = spellAndChanged.first
                    )
                else
                    SpellsByBookViewModel
                        .Event
                        .AddToBook(
                            bookId = bookId,
                            spell = spellAndChanged.first
                        )
            )
        }
    }

    viewModel.onEvent(
        SpellsByBookViewModel
            .Event
            .UpdateListByFilterAndSorter(
                bookId = bookId,
                filter = filter,
                sorter = sorter
            )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.spells) { spellAndChanged ->
            SpellListItem(
                navigate = { navigateFunc(spellAndChanged) },
                transferToBook = transferToBookFunc,
                spellAndChanged = spellAndChanged,
            )
        }
    }
}

@Composable
private fun SpellListItem(
    navigate: () -> Unit,
    transferToBook: (Pair<SpellShortModel, Boolean>) -> Unit,
    spellAndChanged: Pair<SpellShortModel, Boolean>
) {
    var spellAndChangedState by remember { mutableStateOf(spellAndChanged) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(16.dp)
                .clickable(onClick = navigate),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = spellAndChanged.first.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = spellAndChanged.first.level?.toResString() ?: "N/A"
            )
        }
        IconButton(
            modifier = Modifier
                .padding(8.dp),
            onClick = {
                spellAndChangedState = spellAndChangedState
                    .copy(second = !spellAndChangedState.second)
                transferToBook(spellAndChangedState)
            }
        ) {
            if (spellAndChangedState.second)
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    tint = Color.Green,
                    contentDescription = null
                )
            else
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove_24),
                    tint = Color.Red,
                    contentDescription = null
                )
        }
    }
}

@Preview
@Composable
private fun SpellListItemPreview() {
    SpellListItem(
        navigate = {},
        transferToBook = {},
        spellAndChanged = Pair(
            SpellShortModel(
                "vpnavoi",
                "testtesttesttesttesttesttesttest",
                LevelEnum.LEVEL_1
            ),
            false
        )
    )
}