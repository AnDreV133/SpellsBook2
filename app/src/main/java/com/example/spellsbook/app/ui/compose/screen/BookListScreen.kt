package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.item.BookItem
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    getAllBooksUseCase: GetAllBooksUseCase,
) : ViewModel() {
    data class State(
        val books: List<BookModel> = emptyList(),
        val isAddDialogShowing: Boolean = false,
        val bookModelForRemoveDialog: BookModel? = null,
    )

    sealed class Event {
        object ShowAddBookDialog : Event()
        object CloseAddBookDialog : Event()
        class ShowRemoveBookDialog(val model: BookModel) : Event()
        object CloseRemoveBookDialog : Event()
    }

    private val books = getAllBooksUseCase
        .execute()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _state = MutableStateFlow(State())
    val state = combine(_state, books) { state, books ->
        state.copy(books = books)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), State())

    fun onEvent(event: Event) {
        when (event) {
            is Event.ShowAddBookDialog -> {
                _state.value = _state.value.copy(isAddDialogShowing = true)
            }

            is Event.CloseAddBookDialog -> {
                _state.value = _state.value.copy(isAddDialogShowing = false)
            }

            is Event.ShowRemoveBookDialog -> {
                _state.value = _state.value.copy(bookModelForRemoveDialog = event.model)
            }

            is Event.CloseRemoveBookDialog -> {
                _state.value = _state.value.copy(bookModelForRemoveDialog = null)
            }
        }
    }
}


@Composable
fun BooksScreen(
    navController: NavHostController,
    viewModel: BooksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddFloatingButton { viewModel.onEvent(BooksViewModel.Event.ShowAddBookDialog) }
        },

        content = { padding ->
            BookList(
                navController = navController,
                padding = padding,
                viewModel = viewModel
            )
        }
    )

    if (state.isAddDialogShowing)
        AddBookDialog(
            onClose = { viewModel.onEvent(BooksViewModel.Event.CloseAddBookDialog) }
        )

    if (state.bookModelForRemoveDialog != null)
        RemoveBookDialog(
            bookModel = state.bookModelForRemoveDialog!!,
            onClose = { viewModel.onEvent(BooksViewModel.Event.CloseRemoveBookDialog) }
        )
}

@Composable
fun BookList(
    navController: NavController,
    padding: PaddingValues,
    viewModel: BooksViewModel
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(padding),
        contentPadding = PaddingValues(4.dp)
    ) {
        this@LazyColumn.items(state.books) { elem ->
            BookItem(
                model = elem,
                onRemove = { viewModel.onEvent(BooksViewModel.Event.ShowRemoveBookDialog(elem)) },
                navController = navController
            )
        }
    }
}




