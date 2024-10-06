package com.example.spellsbook.data.store.entity.tags

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = LevelEntity.TABLE_NAME,
)
data class LevelEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(COLUMN_ID)
    override val id: Long = 0,
    @ColumnInfo(COLUMN_TAG)
    override val tag: String,
    @ColumnInfo(COLUMN_SPELL_UUID)
    override val spellUuid: String,
) : BaseTagEntity() {
    companion object {
        const val TABLE_NAME = "level_tag"
    }
}