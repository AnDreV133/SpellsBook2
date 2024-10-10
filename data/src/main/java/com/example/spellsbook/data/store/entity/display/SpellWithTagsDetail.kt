package com.example.spellsbook.data.store.entity.display

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import java.util.UUID

class SpellWithTagsDetail (

    @Embedded
    val taggingSpell: TaggingSpellEntity,
    @Relation(
        parentColumn = TaggingSpellEntity.COLUMN_UUID,
        entityColumn = SpellEntity.COLUMN_SPELL_UUID,
        entity = SpellEntity::class
    )
    val spell: SpellEntity
)

