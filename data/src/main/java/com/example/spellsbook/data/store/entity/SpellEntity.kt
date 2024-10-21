package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = SpellEntity.TABLE_NAME,
//    primaryKeys = [
//        SpellEntity.COLUMN_UUID,
//        SpellEntity.COLUMN_LOCALE
//    ],
    foreignKeys = [
        ForeignKey(
            entity = TaggingSpellEntity::class,
            parentColumns = [TaggingSpellEntity.COLUMN_UUID],
            childColumns = [SpellEntity.COLUMN_UUID]
        )
    ],
)
class SpellEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
    @ColumnInfo(name = COLUMN_LOCALE)
    val locale: String,
    @ColumnInfo(name = COLUMN_NAME)
    val spellName: String,
    @ColumnInfo(name = COLUMN_JSON)
    val json: String
) {
    companion object {
        const val TABLE_NAME = "spell"
        const val COLUMN_ID = "id"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_LOCALE = "locale"
        const val COLUMN_NAME = "name"
        const val COLUMN_JSON = "json"
    }
}