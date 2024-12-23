package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsShortByBookIdUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellFromBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KnownSpellsViewModel @Inject constructor(
    val getSpellsShortByBookId: GetSpellsShortByBookIdUseCase,
    val removeSpellFromBook: RemoveSpellFromBookUseCase
) : ViewModel()

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KnownSpellsScreen(
    bookId: Long,
    navController: NavController,
    viewModel: KnownSpellsViewModel = hiltViewModel()
) {
    val state = viewModel
        .getSpellsShortByBookId
        .execute(bookId)
        .map { models ->
            mutableMapOf<LevelEnum, List<SpellShortModel>>().apply {
                putAll(LevelEnum.entries.associateWith { emptyList() })
                models.forEach { model ->
                    if (model.level != null)
                        computeIfPresent(model.level!!) { _, v -> v + model }
                }
            }
        }
        .collectAsState(initial = emptyMap())

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        state.value.forEach { (levelEnum, spells) ->
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.LightGray),
                    text = levelEnum.toResString())
            }

            items(spells) { spell ->
                SpellListItemWithRemoveButton(
                    spell = spell,
                    onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.removeSpellFromBook.execute(bookId, spell.uuid)
                        }
                    }
                ) {
                    navController.navigate(
                        NavEndpoint.SpellByUuid(spell.uuid)
                    )
                }
            }
        }
    }
}