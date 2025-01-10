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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.saturation
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview

private object CustomButtonDefaults {
    private const val saturation = 1.2f

    val buttonFirstColors
        @Composable
        get() = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.firstBackgroundColor,
            contentColor = AppTheme.colors.firstForegroundColor,
            disabledContainerColor = AppTheme.colors.firstBackgroundColor.saturation(saturation),
            disabledContentColor = AppTheme.colors.firstForegroundColor.saturation(saturation)
        )

    val buttonSecondColors: ButtonColors
        @Composable
        get() = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.secondBackgroundColor,
            contentColor = AppTheme.colors.secondForegroundColor,
            disabledContainerColor = AppTheme.colors.secondBackgroundColor.saturation(saturation),
            disabledContentColor = AppTheme.colors.secondForegroundColor.saturation(saturation)
        )

    val buttonFirstBorderStroke: BorderStroke
        @Composable
        get() = BorderStroke(1.dp, AppTheme.colors.secondBackgroundColor)

    val buttonSecondBorderStroke: BorderStroke
        @Composable
        get() = BorderStroke(1.dp, AppTheme.colors.firstBackgroundColor)

    val iconButtonFirstColors: IconButtonColors
        @Composable
        get() = buttonSecondColors.let {
            IconButtonDefaults.iconButtonColors(
                containerColor = it.containerColor,
                contentColor = it.contentColor,
                disabledContainerColor = it.disabledContainerColor,
                disabledContentColor = it.disabledContentColor
            )
        }

    val iconButtonSecondColors: IconButtonColors
        @Composable
        get() = buttonFirstColors.let {
            IconButtonDefaults.iconButtonColors(
                containerColor = it.containerColor,
                contentColor = it.contentColor,
                disabledContainerColor = it.disabledContainerColor,
                disabledContentColor = it.disabledContentColor
            )
        }

    val cornerShape = RoundedCornerShape(20)
}

@Composable
fun CustomFirstIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = CustomButtonDefaults.iconButtonFirstColors,
        content = content
    )
}

@Composable
fun CustomSecondIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = CustomButtonDefaults.iconButtonSecondColors,
        content = content
    )
}


@Composable
fun CustomFirstButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CustomButtonDefaults.cornerShape,
        colors = CustomButtonDefaults.buttonFirstColors,
        elevation = elevation,
        border = CustomButtonDefaults.buttonFirstBorderStroke,
        content = content
    )
}

@Composable
fun CustomSecondButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CustomButtonDefaults.cornerShape,
        colors = CustomButtonDefaults.buttonSecondColors,
        elevation = elevation,
        border = CustomButtonDefaults.buttonSecondBorderStroke,
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

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun CustomFirstIconButtonPreview() {
    AppTheme {
        CustomFirstIconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_24),
                contentDescription = null,
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun CustomSecondIconButtonPreview() {
    AppTheme {
        CustomSecondIconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_24),
                contentDescription = null,
            )
        }
    }
}