package com.example.spellsbook.data.store.entity.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

class SpellWithTagsDetail (
    @Embedded
    val taggingSpell: TaggingSpellEntity,
    @Embedded
    val spell: SpellEntity
)

