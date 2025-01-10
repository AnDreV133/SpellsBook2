package com.example.spellsbook.app.ui.compose.screen.spells.holder

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spellsbook.app.ui.compose.keyboardAsState
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview


@Composable
fun SearchRow(
    query: String,
    onValueChange: (String) -> Unit,
    onClickSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = AppTheme.textStyles.primaryTextStyle,
        trailingIcon = {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { onClickSearch(); focusManager.clearFocus() },
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = AppTheme.colors.cellStrokeFocusedColor,
            focusedContainerColor = AppTheme.colors.cellColor,
            focusedLabelColor = AppTheme.colors.cellTextColor,
            unfocusedIndicatorColor = AppTheme.colors.cellStrokeUnfocusedColor,
            unfocusedContainerColor = AppTheme.colors.cellColor,
            unfocusedLabelColor = AppTheme.colors.cellTintTextColor,
        ),
        shape = RoundedCornerShape(100),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,

        ),
        keyboardActions = KeyboardActions(
            onSearch = { onClickSearch(); focusManager.clearFocus() }
        )
    )
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun SearchRowPreview() {
    AppTheme {
        SearchRow(
            query = "ЁЁЁЁЁЁ",
            onValueChange = {},
            onClickSearch = {}
        )
    }
}