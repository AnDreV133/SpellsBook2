package com.example.spellsbook.app.ui.compose.screen.spells.holder

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SearchRow(
    callbackSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val cbSearch = remember {
        { callbackSearchQuery(query) }
    }

    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = { query = it },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 20.sp
        ),
        trailingIcon = {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = cbSearch,
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black
        ),
        shape = RoundedCornerShape(100),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { cbSearch() }
        )
    )
}

@Preview
@Composable
fun SearchRowPreview() {
    SearchRow(
        modifier = Modifier.width(700.dp),
        callbackSearchQuery = {}
    )
}