package com.example.spellsbook.app.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class BookByIdViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    private val _books = MutableStateFlow<List<BookModel>>(emptyList())
    val books = _books.asStateFlow()

    init { // todo delete when be test database
        runBlocking {
            listOf(
                BookModel(name = "Book 1").also { it.id = 1 },
                BookModel(name = "Book 2").also { it.id = 2 },
                BookModel(name = "Book 3").also { it.id = 3 },
                BookModel(name = "Book 4").also { it.id = 4 }
            ).forEach { addBookUseCase.execute(it) }
        }
    }

    fun executeAllBooks() {
        viewModelScope.launch {
            _books.emit(getAllBooksUseCase.execute())
        }
    }
}

@Composable
fun BookByIdScreen(
    id: Long,
    navController: NavHostController,
) {
    Text(text = "$id - book")

//    Scaffold(
//        floatingActionButton = {
//            AddBookFloatingButton(viewModel)
//        },
//
//        content = { padding ->
//            BookList(
//                padding = padding,
//                viewModel = viewModel
//            )
//        }
//    )
}





