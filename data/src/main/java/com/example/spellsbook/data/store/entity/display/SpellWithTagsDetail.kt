package com.example.spellsbook.data.store.entity.display

import androidx.room.Embedded
import androidx.room.Relation
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.SpellEntityConstant
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

class SpellWithTagsDetail (

    @Embedded
    val taggingSpell: TaggingSpellEntity,
    @Relation(
        parentColumn = TaggingSpellEntity.COLUMN_UUID,
        entityColumn = SpellEntityConstant.COLUMN_UUID,
        entity = SpellEntity::class
    )
    val spell: SpellEntity
)

