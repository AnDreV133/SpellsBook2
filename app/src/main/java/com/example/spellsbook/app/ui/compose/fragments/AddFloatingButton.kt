package com.example.spellsbook.app.ui.compose.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spellsbook.R

@Composable
fun AddFloatingButton(
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier
            .background(Color.White, shape = CircleShape)
            .padding(8.dp)
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