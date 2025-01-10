package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.fragments.CustomSecondButton
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.ValidateBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDialogViewModel @Inject constructor(
    private val validateBookUseCase: ValidateBookUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    data class State(
        val bookName: String = "",
        val bookNameError: String? = null
    )

    sealed class Event {
        class Edit(val bookModel: BookModel) : Event()
        class CloseDialog(val onClose: () -> Unit) : Event()
        class AddWithValidate(val onClose: () -> Unit) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.Edit -> {
                _state.value = state.value.copy(
                    bookName = event.bookModel.name
                )
            }

            is Event.AddWithValidate -> {
                val model = BookModel(
                    name = state.value.bookName
                )

                validateBookUseCase.execute(model).also { validationResult ->
                    if (validationResult.successful) {
                        viewModelScope.launch { addBookUseCase.execute(model) }
                        closeDialog(onClose = event.onClose)
                    } else {
                        _state.value = state.value.copy(
                            bookNameError = validationResult.errorMessage
                        )
                    }
                }
            }

            is Event.CloseDialog -> {
                closeDialog(onClose = event.onClose)
            }
        }
    }

    private fun closeDialog(onClose: () -> Unit) {
        _state.value = State()
        onClose()
    }
}


@Composable
fun AddBookDialogScreen(
    onClose: () -> Unit,
    viewModel: AddDialogViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    AddBookDialog(
        state = state,
        onClose = onClose,
        onComplete = {
            viewModel.onEvent(
                AddDialogViewModel.Event.AddWithValidate(
                    onClose = onClose
                )
            )
        },
        onEditBookName = {
            viewModel.onEvent(
                AddDialogViewModel.Event.Edit(
                    BookModel(name = it)
                )
            )
        }
    )
}

@Composable
private fun AddBookDialog(
    state: AddDialogViewModel.State,
    onClose: () -> Unit,
    onEditBookName: (String) -> Unit,
    onComplete: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(AppTheme.colors.secondBackgroundColor, shape = CardDefaults.shape)
                .padding(16.dp)
                .border(20.dp, AppTheme.colors.secondBackgroundColor)
                .padding(20.dp)
                .wrapContentSize()
        ) {
            Text(
                text = stringResource(R.string.title_add_book),
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp),
                style = AppTheme.textStyles.primaryBoldTextStyle
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.name),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(
                        bottom = 8.dp,
                        end = 8.dp
                    )

                )
                TextField(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Min),
                    value = state.bookName,
                    onValueChange = onEditBookName,
                )
            }

            CustomSecondButton(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Min),
                onClick = onComplete
            ) {
                Text(
                    text = stringResource(R.string.add),
                    style = AppTheme.textStyles.primaryTextStyle
                )
            }

            if (state.bookNameError != null) {
                Text(
                    text = state.bookNameError,
                    color = Color.Red,
                    style = AppTheme.textStyles.smallTextStyle
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddBookDialogPreviewWithoutData() {
    AppTheme {
        AddBookDialog(
            state = AddDialogViewModel.State(
                bookName = "",
                bookNameError = null
            ),
            onClose = {},
            onEditBookName = {},
            onComplete = {}
        )
    }
}

@Preview
@Composable
private fun AddBookDialogPreviewWithData() {
    AppTheme {
        AddBookDialog(
            state = AddDialogViewModel.State(
                bookName = "1Грегор",
                bookNameError = "Проблема ввода названия книги"
            ),
            onClose = {},
            onEditBookName = {},
            onComplete = {}
        )
    }
}