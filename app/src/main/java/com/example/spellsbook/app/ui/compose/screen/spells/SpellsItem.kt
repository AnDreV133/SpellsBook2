package com.example.spellsbook.app.ui.compose.screen.spells

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.app.ui.theme.AppTheme
import com.example.spellsbook.app.ui.theme.backgroundColorForPreview
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.model.SpellShortModel

@Composable
fun SpellListItemWithRemoveButton(
    spell: SpellShortModel,
    onClick: () -> Unit,
    navigate: () -> Unit
) {
    val textWidth = 0.7f

    Row(
        modifier = Modifier
            .background(
                color = AppTheme.colors.secondBackgroundColor,
                shape = RoundedCornerShape(20)
            )
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpellListItem(
            modifier = Modifier
                .weight(weight = textWidth),
            spell = spell,
            navigate = navigate
        )
        IconButton(
            modifier = Modifier
                .padding(8.dp),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove_24),
                tint = Color.Red,
                contentDescription = null
            )
        }
    }
}


@Composable
fun SpellListItemWithSwitchButton(
    spellAndChanged: Pair<SpellShortModel, Boolean>,
    addToBook: (SpellShortModel) -> Unit,
    removeFromBook: (SpellShortModel) -> Unit,
    navigate: () -> Unit
) {
    val textWidth = 0.7f

    Row(
        modifier = Modifier
            .background(
                AppTheme.colors.secondBackgroundColor,
                RoundedCornerShape(20)
            )
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpellListItem(
            spell = spellAndChanged.first,
            navigate = navigate,
            modifier = Modifier
                .weight(weight = textWidth)
        )
        if (spellAndChanged.second)
            IconButton(
                modifier = Modifier
                    .padding(8.dp),
                onClick = {
                    addToBook(spellAndChanged.first)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    tint = Color.Green,
                    contentDescription = null
                )
            }
        else
            IconButton(
                modifier = Modifier
                    .padding(8.dp),
                onClick = {
                    removeFromBook(spellAndChanged.first)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove_24),
                    tint = Color.Red,
                    contentDescription = null
                )
            }
    }
}

@Composable
fun SpellListItem(
    spell: SpellShortModel,
    modifier: Modifier = Modifier,
    navigate: () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                AppTheme.colors.secondBackgroundColor,
                RoundedCornerShape(20)
            )
            .clickable(onClick = navigate)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = spell.name,
                color = AppTheme.colors.secondForegroundColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.textStyles.primaryBoldTextStyle
            )
            Text(
                text = spell.level?.toResString() ?: "N/A",
                color = AppTheme.colors.secondForegroundColor,
                style = AppTheme.textStyles.smallTextStyle
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
private fun SpellListItemPreview() {
    AppTheme {
        SpellListItem(
            spell = SpellShortModel(
                "vpnavoi",
                "testtesttesttesttesttesttesttesttesttesttesttesttesttest",
                LevelEnum.LEVEL_1
            ),
            navigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
private fun SpellListItemWithRemoveButtonPreview() {
    AppTheme {
        SpellListItemWithRemoveButton(
            SpellShortModel(
                "vpnavoi",
                "testtesttesttesttesttesttesttesttesttesttesttesttesttest",
                LevelEnum.LEVEL_1
            ),
            onClick = {}
        ) {}
    }
}

@Preview(
    showBackground = true,
    backgroundColor = backgroundColorForPreview
)
@Composable
fun SpellListItemWithSwitchButtonPreview() {
    AppTheme {
        SpellListItemWithSwitchButton(
            Pair(
                SpellShortModel(
                    "vpnavoi",
                    "testtesttesttesttesttesttesttesttesttesttesttesttesttest",
                    LevelEnum.LEVEL_1
                ),
                true
            ),
            {},
            {},
            {}
        )
    }
}
