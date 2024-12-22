package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.spellsbook.domain.usecase.GetSpellsShortByBookIdUseCase
import com.example.spellsbook.domain.usecase.RemoveSpellFromBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KnownSpellsViewModel @Inject constructor(
    val getSpellsShortByBookId: GetSpellsShortByBookIdUseCase,
    val removeSpellFromBook: RemoveSpellFromBookUseCase
) : ViewModel()

@Composable
fun KnownSpellsScreen(
    bookId: Long,
    navController: NavHostController,
    viewModel: KnownSpellsViewModel = hiltViewModel()
) {
    Text("stub")
}