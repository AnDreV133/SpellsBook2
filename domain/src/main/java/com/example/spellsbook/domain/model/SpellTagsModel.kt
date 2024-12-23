package com.example.spellsbook.domain.model

import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.RangeEnum

data class SpellTagsModel(
    val level: LevelEnum? = null,
    val castingTime: CastingTimeEnum? = null,
    val school: SchoolEnum? = null,
    val range: RangeEnum? = null,
    val source: SourceEnum? = null,
    val ritual: RitualEnum? = null
)
