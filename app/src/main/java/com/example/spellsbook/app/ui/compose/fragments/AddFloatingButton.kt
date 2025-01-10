package com.example.spellsbook.app.ui.compose.fragments

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview

@Composable
fun AddFloatingButton(
    onClick: () -> Unit,
) {
    CustomFirstIconButton(
        modifier = Modifier
            .size(60.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_24),
            contentDescription = "Add button",
            modifier = Modifier
                .size(48.dp),
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun AddFloatingButtonPreview() {
    AppTheme {
        AddFloatingButton(onClick = {})
    }
}