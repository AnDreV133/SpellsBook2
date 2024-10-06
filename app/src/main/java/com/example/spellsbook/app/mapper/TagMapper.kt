package com.example.spellsbook.app.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.spellsbook.R
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum

@Composable
fun LevelEnum.toResourceString() =
    stringResource(
        id = when (this) {
            LevelEnum.LEVEL_0 -> R.string.tag_level_0
            LevelEnum.LEVEL_1 -> R.string.tag_level_1
            LevelEnum.LEVEL_2 -> R.string.tag_level_2
            LevelEnum.LEVEL_3 -> R.string.tag_level_3
            LevelEnum.LEVEL_4 -> R.string.tag_level_4
            LevelEnum.LEVEL_5 -> R.string.tag_level_5
            LevelEnum.LEVEL_6 -> R.string.tag_level_6
            LevelEnum.LEVEL_7 -> R.string.tag_level_7
            LevelEnum.LEVEL_8 -> R.string.tag_level_8
            LevelEnum.LEVEL_9 -> R.string.tag_level_9
        }
    )

@Composable
fun SchoolEnum.toResourceString() =
    stringResource(
        id = when (this) {
            SchoolEnum.ABJURATION -> R.string.tag_school_abjuration
            SchoolEnum.CONJURATION -> R.string.tag_school_conjuration
            SchoolEnum.DIVINATION -> R.string.tag_school_divination
            SchoolEnum.ENCHANTMENT -> R.string.tag_school_enchantment
            SchoolEnum.TRANSMUTATION -> R.string.tag_school_transmutation
            SchoolEnum.ILLUSION -> R.string.tag_school_illusion
            SchoolEnum.INVOCATION -> R.string.tag_school_invocation
            SchoolEnum.NECROMANCY -> R.string.tag_school_necromancy
        }
    )
