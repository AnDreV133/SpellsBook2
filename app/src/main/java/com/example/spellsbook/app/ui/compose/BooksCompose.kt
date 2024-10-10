package com.example.spellsbook.app.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.GetAllBooksUseCase
import com.example.spellsbook.domain.usecase.ValidateBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

//@HiltViewModel
//class AddDialogViewModel @Inject constructor(
//    private val validateBookUseCase: ValidateBookUseCase,
//    private val addBookUseCase: AddBookUseCase
//) : ViewModel() {
//    data class State(
//        val bookName: String,
//        val bookNameError: String? = null
//    )
//
//    val state = mutableStateOf(State(""))
//
//    fun addBookWithValidate(bookModel: BookModel) =
//        validateBookUseCase.execute(bookModel).also { validationResult ->
//            if (validationResult.successful) {
//                addBook(bookModel)
//            } else {
//                state.value = state.value.copy(
//                    bookNameError = validationResult.errorMessage
//                )
//            }
//        }
//
//    fun updateBookName(name: String) {
//        state.value = state.value.copy(
//            bookName = name
//        )
//    }
//
//
//    private fun addBook(model: BookModel) {
//        viewModelScope.launch {
//            addBookUseCase.execute(model)
//        }
//    }
//
//    fun clearState() {
//        state.value = State("")
//    }
//}

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val validateBookUseCase: ValidateBookUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    data class DialogState(
        val bookName: MutableState<String> = mutableStateOf(""),
        val bookNameError: MutableState<String?> = mutableStateOf(null)
    )

    data class State(
        val books: List<BookModel> = emptyList(),
        val isDialogShowing: Boolean = false,
        val dialogState: DialogState = DialogState()
    )

    sealed class Event {
        object ShowAddBookDialog : Event()
        class UpdateBookEdit(val bookModel: BookModel) : Event()
        object SaveBook : Event()
        object CloseDialog : Event()
//        class UpdateBooks : Event()
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

    init { // todo remove when be test database
        runBlocking {
            listOf(
                BookModel(name = "Book 1"),
                BookModel(name = "Book 2"),
                BookModel(name = "Book 3"),
                BookModel(name = "Book 4"),
            ).forEach { addBookUseCase.execute(it) }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.ShowAddBookDialog -> {
                _state.value = _state.value.copy(isDialogShowing = true)
            }

            is Event.CloseDialog -> {
                _state.value = _state.value.copy(isDialogShowing = false)
            }

            is Event.SaveBook -> {
                viewModelScope.launch {
                    validateBookUseCase
                        .execute(
                            BookModel(name = _state.value.dialogState.bookName.value)
                        )
                        .also { validationResult ->
                            if (validationResult.successful) {
                                addBookUseCase.execute(
                                    BookModel(
                                        name = _state.value.dialogState.bookName.value
                                    )
                                )
                            } else {
                                _state.value.dialogState.bookNameError.value =
                                    validationResult.errorMessage
                            }
                        }
                }
            }

            is Event.UpdateBookEdit -> {
                viewModelScope.launch {
                    _state.value.dialogState.bookName.value = event.bookModel.name
                }
            }
        }
    }
}


class BooksCompose(private val navController: NavHostController) {
//    private var isDialogShowing = mutableStateOf(false)

    @Composable
    fun BooksScreen(
        viewModel: BooksViewModel = hiltViewModel(),
    ) {
        Scaffold(
            floatingActionButton = {
                AddBookFloatingButton(viewModel)
            },

            content = { padding ->
                BookList(
                    padding = padding,
                    viewModel = viewModel
                )
            }
        )
    }


    @Composable
    fun BookList(
        padding: PaddingValues,
        viewModel: BooksViewModel
    ) {
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(viewModel.state.value.books) { elem ->
                BookItem(model = elem)
            }
        }
    }

    @Composable
    fun BookItem(
        model: BookModel
    ) {
        Card(
            modifier = Modifier
                .padding(
                    horizontal = 4.dp,
                    vertical = 8.dp,
                )
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray,
            ),
            onClick = {
//                println("click on item id: ${model.id}")
                navController.navigate(NavEndpoint.BookById(model.id))
            }
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
                color = Color.Black,
                textAlign = TextAlign.Start,
                text = model.name,
                fontSize = 24.sp
            )
        }
    }

    @Composable
    fun AddBookFloatingButton(
        viewModel: BooksViewModel
    ) {
        val state by viewModel.state.collectAsState()

        IconButton(
            modifier = AddButtonShape()
                .padding(8.dp)
                .size(60.dp),
            onClick = { viewModel.onEvent(BooksViewModel.Event.ShowAddBookDialog) }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_24),
                contentDescription = "Add Book",
                modifier = Modifier
                    .size(48.dp),
            )
        }



        if (state.isDialogShowing)
            AddBookDialog(
                forClose = {
                    viewModel.onEvent(BooksViewModel.Event.CloseDialog)
                },
            )
    }
}
