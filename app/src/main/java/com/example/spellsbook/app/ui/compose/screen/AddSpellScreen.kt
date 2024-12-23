package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.screen.spells.SpellListItemWithButton
import com.example.spellsbook.domain.model.SpellShortModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AddSpellListViewModel(
    // todo remove
    // todo addUpdate
    // todo get
) : ViewModel() {}

@Composable
fun AddSpellListScreen(
    navController: NavController,
    viewModel: AddSpellListViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            Text("My spells")
        },
        floatingActionButton = {
            AddFloatingButton {
                navController.navigate( TODO("navigate to addSpellScreen" ) as String)
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items (listOf(TODO()) as List<SpellShortModel>) {
                SpellListItemWithButton(spell = it, onClick = { /*TODO*/ }) {

                }
            }
        }
    }
}