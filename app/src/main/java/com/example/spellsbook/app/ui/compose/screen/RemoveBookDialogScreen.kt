package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.fragments.CustomSecondButton
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.RemoveBookDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveBookDialogViewModel @Inject constructor(
    private val removeBookUseCase: RemoveBookDialogUseCase,
) : ViewModel() {
    sealed class Event {
        class RemoveBook(val bookModel: BookModel) : Event()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.RemoveBook -> {
                viewModelScope.launch {
                    removeBookUseCase.execute(event.bookModel)
                }
            }
        }
    }
}

@Composable
fun RemoveBookDialogScreen(
    bookModel: BookModel,
    onClose: () -> Unit,
    viewModel: RemoveBookDialogViewModel = hiltViewModel()
) {
    RemoveBookDialog(
        model = bookModel,
        onClose = onClose,
        onAgree = {
            viewModel.onEvent(
                RemoveBookDialogViewModel.Event.RemoveBook(bookModel)
            )
        }
    )
}

@Composable
private fun RemoveBookDialog(
    model: BookModel,
    onClose: () -> Unit,
    onAgree: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .wrapContentSize(),
        text = {
            Text(
                text = stringResource(id = R.string.remove_fmt, model.name),
                style = AppTheme.textStyles.largeBoldTextStyle
            )
        },
        containerColor = AppTheme.colors.secondBackgroundColor,
        onDismissRequest = onClose,
        confirmButton = {
            CustomSecondButton(
                onClick = {
                    onAgree()
                    onClose()
                }
            ) {
                Text(
                    stringResource(id = R.string.remove),
                    style = AppTheme.textStyles.smallTextStyle
                )
            }
        }
    )
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
private fun RemoveBookDialogScreenPreview() {
    AppTheme {
        RemoveBookDialog(
            model = BookModel(
                name = "Грегор"
            ),
            onClose = {},
            onAgree = {}
        )
    }
}