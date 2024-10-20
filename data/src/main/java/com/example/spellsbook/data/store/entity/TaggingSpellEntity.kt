package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TaggingSpellEntity.TABLE_NAME)
data class TaggingSpellEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
    @ColumnInfo(name = COLUMN_LEVEL_TAG)
    val levelTag: String? = null,
    @ColumnInfo(name = COLUMN_SCHOOL_TAG)
    val schoolTag: String? = null,

    // todo add more columns here
) {
    companion object {
        const val TABLE_NAME = "tagging_spell"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_SCHOOL_TAG = "school_tag"
        const val COLUMN_LEVEL_TAG = "level_tag"

        // todo add more names for columns here
    }
}