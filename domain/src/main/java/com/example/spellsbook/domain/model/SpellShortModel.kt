package com.example.spellsbook.domain.model

import com.example.spellsbook.domain.enums.LevelEnum
import java.util.UUID

data class SpellShortModel (
    val spellUuid: UUID,
    val name: String,
    val level: LevelEnum?
)


