package com.example.spellsbook.data.store.entity.display

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

class SpellWithTagsShort(

    @Embedded
    val taggingSpell: TaggingSpellEntity,
    @Relation(
        parentColumn = TaggingSpellEntity.COLUMN_UUID,
        entityColumn = SpellEntity.COLUMN_UUID,
        entity = SpellEntity::class
    )
    val spell: SpellShort
) {
    class SpellShort(
//        @ColumnInfo(name = SpellEntity.COLUMN_ID)
//        val id: Long,
        @ColumnInfo(name = SpellEntity.COLUMN_UUID)
        val uuid: String,
//        @ColumnInfo(name = SpellEntity.COLUMN_LOCALE)
//        val locale: String,
        @ColumnInfo(name = SpellEntity.COLUMN_NAME)
        val name: String
    )
}

