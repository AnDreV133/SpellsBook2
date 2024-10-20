package com.example.spellsbook.domain.model

import com.example.spellsbook.domain.enums.LevelEnum

data class SpellShortModel (
    val spellUuid: String,
    val name: String,
    val level: LevelEnum?
)


