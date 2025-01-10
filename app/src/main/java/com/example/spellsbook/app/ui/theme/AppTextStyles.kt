package com.example.spellsbook.app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BaseTextStyle = TextStyle(
    fontSize = 20.sp
)

val BaseBoldTextStyle = BaseTextStyle.copy(
    fontWeight = FontWeight.Bold
)

val SmallTextStyle = BaseTextStyle.copy(
    fontSize = 16.sp
)

val SmallBoldTextStyle = SmallTextStyle.copy(
    fontWeight = FontWeight.Bold
)

val LargeTextStyle = BaseTextStyle.copy(
    fontSize = 24.sp
)

val LargeBoldTextStyle = LargeTextStyle.copy(
    fontWeight = FontWeight.Bold
)