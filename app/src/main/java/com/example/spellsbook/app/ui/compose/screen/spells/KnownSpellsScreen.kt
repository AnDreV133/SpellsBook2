package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.app.ui.theme.AppColors
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.appNavController
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsShortByBookIdUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellFromBookUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = KnownSpellsViewModel.ViewModelFactory::class)
class KnownSpellsViewModel @AssistedInject constructor(
    @Assisted private val bookId: Long,
    private val getSpellsShortByBookId: GetSpellsShortByBookIdUseCase,
    private val removeSpellFromBook: RemoveSpellFromBookUseCase
) : ViewModel() {
    @AssistedFactory
    interface ViewModelFactory {
        fun create(bookId: Long): KnownSpellsViewModel
    }

    data class State(
        val spells: Map<LevelEnum, List<SpellShortModel>> = emptyMap()
    )

    sealed class Event {
        class RemoveSpell(val spellUuid: String) : Event()
        class OpenScreen(val bookId: Long) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) = when (event) {
        is Event.RemoveSpell -> viewModelScope.launch {
            removeSpellFromBook.execute(bookId, event.spellUuid)
        }

        is Event.OpenScreen -> viewModelScope.launch {
            getSpellsShortByBookId
                .execute(event.bookId)
                .collect { models ->
                    _state.value = _state.value.copy(
                        spells = getGroupedSpellsByLevel(models)
                    )
                }
        }
    }

    private fun getGroupedSpellsByLevel(models: List<SpellShortModel>): Map<LevelEnum, List<SpellShortModel>> =
        mutableMapOf<LevelEnum, List<SpellShortModel>>().apply {
            putAll(LevelEnum.entries.associateWith { emptyList() })
            models.forEach { model ->
                if (model.level != null)
                    computeIfPresent(model.level!!) { _, v -> v + model }
            }
        }
}

@Composable
fun KnownSpellsScreen(
    bookId: Long,
    navController: NavController = appNavController,
    viewModel: KnownSpellsViewModel =
        hiltViewModel { factory: KnownSpellsViewModel.ViewModelFactory ->
            factory.create(bookId)
        }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(KnownSpellsViewModel.Event.OpenScreen(bookId))
    }

    KnownSpellsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navigateToDetail = { uuid ->
            navController.navigate(
                NavEndpoint.SpellByUuid(uuid)
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KnownSpellsScreen(
    state: KnownSpellsViewModel.State,
    onEvent: (KnownSpellsViewModel.Event) -> Unit,
    navigateToDetail: (uuid: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.firstBackgroundColor)
    ) {
        state.spells.forEach { (levelEnum, spells) ->
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = AppTheme.colors.cellColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = AppTheme.colors.cellStrokeUnfocusedColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(4.dp),
                    text = levelEnum.toResString()
                )
            }

            items(spells) { spell ->
                SpellListItemWithRemoveButton(
                    modifier = Modifier
                        .padding(
                            horizontal = 4.dp,
                            vertical = 8.dp
                        ),
                    spell = spell,
                    onClick = {
                        onEvent(KnownSpellsViewModel.Event.RemoveSpell(spell.uuid))
                    },
                    navigate = { navigateToDetail(spell.uuid) }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
private fun KnownSpellsScreenPreview() {
    AppTheme {
        KnownSpellsScreen(
            state = KnownSpellsViewModel.State(
                spells = mapOf(
                    LevelEnum.LEVEL_3 to listOf(
                        SpellShortModel(
                            uuid = "1",
                            name = "Fireball",
                            level = LevelEnum.LEVEL_3
                        )
                    )
                )
            ),
            onEvent = {},
            navigateToDetail = {}
        )
    }
}

