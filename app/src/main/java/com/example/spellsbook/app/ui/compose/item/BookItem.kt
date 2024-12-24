package com.example.spellsbook.app.ui.compose.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.model.BookModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookItem(
    model: BookModel,
    onRemove: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp,
            )
            .fillMaxSize()
            .combinedClickable(
                onLongClick = { navController.navigate(NavEndpoint.ExportBook(model.id)) },
                onClick = { navController.navigate(NavEndpoint.UnknownSpells(model.id)) }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
        )
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

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete button"
            )
        }
    }
}