package com.example.spellsbook.data.store.entity.spells

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SpellEntityRu.TABLE_NAME)
data class SpellEntityRu(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    override val id: Long = 0,
    @ColumnInfo(name = COLUMN_UUID)
    override val uuid: String,
    @ColumnInfo(name = COLUMN_NAME)
    override val name: String,
    @ColumnInfo(name = COLUMN_JSON)
    override val json: String,
) : SpellEntity() {
    companion object {
        const val TABLE_NAME = "spells_ru"
    }
}