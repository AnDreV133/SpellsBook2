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
    @ColumnInfo(name = COLUMN_CASTING_TIME_TAG)
    val castingTime: String? = null,
    @ColumnInfo(name = COLUMN_RANGE_TAG)
    val range: String? = null,
    @ColumnInfo(name = COLUMN_RITUAL_TAG)
    val ritual: String? = null,
    @ColumnInfo(name = COLUMN_SOURCE_TAG)
    val source: String? = null

    // todo add more columns here
) {
    companion object {
        const val TABLE_NAME = "tagging_spell"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_SCHOOL_TAG = "school_tag"
        const val COLUMN_LEVEL_TAG = "level_tag"
        const val COLUMN_CASTING_TIME_TAG = "casting_time"
        const val COLUMN_RANGE_TAG = "range_tag"
        const val COLUMN_RITUAL_TAG = "ritual_tag"
        const val COLUMN_SOURCE_TAG = "source_tag"

        // todo add more names for columns here
    }
}