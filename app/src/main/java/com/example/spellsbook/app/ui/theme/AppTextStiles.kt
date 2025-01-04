package com.example.spellsbook.app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BaseTextStyle = TextStyle(
    color = DarkMiddleGrayColor,
    fontSize = 20.sp
)

val BaseBoldTextStyle = BaseTextStyle.copy(
    fontWeight = FontWeight.Bold
)

val TintTextStyle = TextStyle(
    color = GrayColor,
    fontSize = 16.sp
)