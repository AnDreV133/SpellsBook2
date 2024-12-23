package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.fragments.ScreenWithMenuBar
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.app.ui.compose.screen.spells.SpellListItemWithRemoveButton
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.AddUpdateSpellByAuthorUseCase
import com.example.spellsbook.domain.usecase.GetSpellsShortByAuthorUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellByAuthorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
        class AddNewSpell(val name: String) : Event()
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
                    addUpdateSpellByAuthorUseCase.executeNew(event.name)
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

    ScreenWithMenuBar(
        menuBar = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 2.dp
                    )
                    Text(
                        text = stringResource(id = R.string.title_my_spells),
                        fontSize = 28.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        floatingButton = {
            val baseName = stringResource(id = R.string.base_name_new_spell)
            AddFloatingButton {
                viewModel.onEvent(
                    AuthorSpellListViewModel.Event.AddNewSpell(baseName)
                )
//                navController.navigate(NavEndpoint.SpellsByUuidWithModifying()) // TODO: navigate to addSpellScreen when contain navigate in DI
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(state.spells) { model ->
                SpellListItemWithRemoveButton(
                    spell = model,
                    onClick = {
                        viewModel.onEvent(
                            AuthorSpellListViewModel
                                .Event.RemoveSpell(model.uuid)
                        )
                    }
                ) {
                    navController.navigate(
                        NavEndpoint.SpellByUuidEditor(model.uuid)
                    )
                }
            }
        }
    }
}