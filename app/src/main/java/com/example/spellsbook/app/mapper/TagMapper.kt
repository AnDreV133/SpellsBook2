package com.example.spellsbook.app.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.spellsbook.R
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.RangeEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum

@Composable
fun TagIdentifierEnum.toResString() = stringResource(
    id = when (this) {
        TagIdentifierEnum.SCHOOL -> R.string.tag_school_name
        TagIdentifierEnum.LEVEL -> R.string.tag_level_name
        TagIdentifierEnum.SOURCE -> R.string.tag_source_name
        TagIdentifierEnum.RANGE -> R.string.tag_range_name
        TagIdentifierEnum.RITUAL -> R.string.tag_ritual_name
        TagIdentifierEnum.CASTING_TIME -> R.string.tag_casting_time_name
    }
)

@Composable
fun TagEnum.toResString() = stringResource(
    id = when (this) {
        is SchoolEnum -> toResString()
        is LevelEnum -> toResString()
        is SourceEnum -> toResString()
        is RangeEnum -> toResString()
        is CastingTimeEnum -> toResString()
        is RitualEnum -> toResString()
        else -> throw IllegalArgumentException("Unknown TagEnum")
    }
)

private fun RangeEnum.toResString() = when (this) {
    RangeEnum.SPECIAL -> R.string.tag_range_special
    RangeEnum.CLOSE -> R.string.tag_range_close
    RangeEnum.VERY_CLOSE -> R.string.tag_range_very_close
    RangeEnum.FAR -> R.string.tag_range_far
    RangeEnum.VERY_FAR -> R.string.tag_range_very_far
}

private fun CastingTimeEnum.toResString() = when (this) {
    CastingTimeEnum.ACTION -> R.string.tag_casting_time_action
    CastingTimeEnum.BONUS_ACTION -> R.string.tag_casting_time_bonus_action
    CastingTimeEnum.REACTION -> R.string.tag_casting_time_reaction
    CastingTimeEnum.MINUTES -> R.string.tag_casting_time_minutes
    CastingTimeEnum.HOURS -> R.string.tag_casting_time_hours
}

private fun SourceEnum.toResString() = when (this) {
    SourceEnum.BY_AUTHOR -> R.string.tag_source_by_author
    SourceEnum.PREPARED -> R.string.tag_source_prepared
}

private fun LevelEnum.toResString() = when (this) {
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


private fun SchoolEnum.toResString() = when (this) {
    SchoolEnum.ABJURATION -> R.string.tag_school_abjuration
    SchoolEnum.CONJURATION -> R.string.tag_school_conjuration
    SchoolEnum.DIVINATION -> R.string.tag_school_divination
    SchoolEnum.ENCHANTMENT -> R.string.tag_school_enchantment
    SchoolEnum.TRANSMUTATION -> R.string.tag_school_transmutation
    SchoolEnum.ILLUSION -> R.string.tag_school_illusion
    SchoolEnum.INVOCATION -> R.string.tag_school_invocation
    SchoolEnum.NECROMANCY -> R.string.tag_school_necromancy
}

private fun RitualEnum.toResString() = when (this) {
    RitualEnum.RITUAL -> R.string.tag_ritual_ritual
    RitualEnum.CAST -> R.string.tag_ritual_cast
}