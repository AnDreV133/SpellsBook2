package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.app.ui.compose.fragments.AddFloatingButton
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.appNavController
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
    viewModel: BooksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddFloatingButton { viewModel.onEvent(BooksViewModel.Event.ShowAddBookDialog) }
        },

        content = { padding ->
            BookList(
                state.books,
                onRemoveBook = { viewModel.onEvent(BooksViewModel.Event.ShowRemoveBookDialog(it)) },
                modifier = Modifier.padding(padding)
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
    books: List<BookModel>,
    onRemoveBook: (model: BookModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = appNavController

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.firstBackgroundColor),
        contentPadding = PaddingValues(4.dp),
    ) {
        this@LazyColumn.items(books) { elem ->
            BookItem(
                model = elem,
                onRemove = { onRemoveBook(elem) },
                navigateExport = { navController.navigate(NavEndpoint.ExportBook(elem.id)) },
                navigateDetail = { navController.navigate(NavEndpoint.UnknownSpells(elem.id)) }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookItem(
    model: BookModel,
    onRemove: () -> Unit,
    navigateExport: () -> Unit,
    navigateDetail: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp,
            )
            .fillMaxSize()
            .combinedClickable(
                onLongClick = navigateExport,
                onClick = navigateDetail
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.secondBackgroundColor,
        )
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
            textAlign = TextAlign.Start,
            text = model.name,
            style = AppTheme.textStyles.primaryBoldTextStyle
        )

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete button",
                tint = AppTheme.colors.textColor
            )
        }
    }
}

@Preview
@Composable
fun PreviewBooksScreen() {
    BooksScreen()
}
