package com.example.spellsbook.app.ui.compose.fragments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spellsbook.app.ui.compose.saturation
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview

@Composable
fun CustomSecondButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(20),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AppTheme.colors.secondBackgroundColor,
        contentColor = AppTheme.colors.secondForegroundColor,
        disabledContainerColor = AppTheme.colors.secondBackgroundColor.saturation(1.2f),
        disabledContentColor = AppTheme.colors.secondForegroundColor.saturation(1.2f)
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = BorderStroke(1.dp, AppTheme.colors.firstBackgroundColor),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Composable
fun CustomFirstButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(20),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AppTheme.colors.firstBackgroundColor,
        contentColor = AppTheme.colors.firstForegroundColor,
        disabledContainerColor = AppTheme.colors.firstBackgroundColor.saturation(1.2f),
        disabledContentColor = AppTheme.colors.firstForegroundColor.saturation(1.2f)
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = BorderStroke(1.dp, AppTheme.colors.secondBackgroundColor),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun CustomFirstButtonPreview() {
    AppTheme {
        CustomFirstButton(onClick = {}) {
            Text(text = "Hello")
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun CustomSecondButtonPreview() {
    AppTheme {
        CustomSecondButton(onClick = {}) {
            Text(text = "Hello")
        }
    }
}