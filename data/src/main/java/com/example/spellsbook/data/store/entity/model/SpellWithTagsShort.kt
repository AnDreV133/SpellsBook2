package com.example.spellsbook.data.store.entity.model

import androidx.room.Embedded
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

class SpellWithTagsShort(
    @Embedded
    val taggingSpell: TaggingSpellEntity,
    val name: String
)

