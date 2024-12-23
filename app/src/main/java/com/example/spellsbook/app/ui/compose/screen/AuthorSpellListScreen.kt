package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.screen.spells.SpellListItemWithRemoveButton
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.usecase.AddUpdateSpellByAuthorUseCase
import com.example.spellsbook.domain.usecase.GetSpellsShortByAuthorUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellByAuthorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthorSpellListViewModel @Inject constructor(
    getSpellsShortByAuthorUseCase: GetSpellsShortByAuthorUseCase,
    private val addUpdateSpellByAuthorUseCase: AddUpdateSpellByAuthorUseCase,
    private val removeSpellByAuthorUseCase: RemoveSpellByAuthorUseCase
) : ViewModel() {
    class State(
        val spells: List<SpellShortModel> = emptyList()
    )

    sealed class Event {
        object AddNewSpell : Event()
        class RemoveSpell(val uuid: String) : Event()
    }

    val state = getSpellsShortByAuthorUseCase
        .execute()
        .map {
            State(spells = it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            State()
        )

    fun onEvent(event: Event) {
        when (event) {
            is Event.AddNewSpell -> {
                viewModelScope.launch {
                    val randomUuid = UUID.randomUUID().toString()

                    addUpdateSpellByAuthorUseCase.execute(
                        SpellTagsModel(),
                        SpellDetailModel(
                            uuid = randomUuid,
                        )
                    )
                }
            }

            is Event.RemoveSpell -> {
                viewModelScope.launch {
                    removeSpellByAuthorUseCase.execute(event.uuid)
                }
            }
        }
    }
}

@Composable
fun AuthorSpellListScreen(
    navController: NavController,
    viewModel: AuthorSpellListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            Text("My spells")
        },
        floatingActionButton = {
            AddFloatingButton {
                viewModel.onEvent(AuthorSpellListViewModel.Event.AddNewSpell)
                //navController.navigate(TODO("navigate to addSpellScreen") as String)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(state.spells) { model ->
                SpellListItemWithRemoveButton(
                    spell = model,
                    onClick = {
                        viewModel.onEvent(AuthorSpellListViewModel.Event.RemoveSpell(model.uuid))
                    }
                ) {
                    //navController.navigate(TODO("navigate to addSpellScreen") as String)
                }
            }
        }
    }
}