package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = SpellEntity.TABLE_NAME,
//    foreignKeys = [
//        ForeignKey(
//            entity = TaggingSpellEntity::class,
//            parentColumns = [TaggingSpellEntity.COLUMN_UUID],
//            childColumns = [SpellEntity.COLUMN_UUID]
//        )
//    ]
)
class SpellEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_JSON)
    val json: String
) {
    companion object {
        const val TABLE_NAME = "spell"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_NAME = "name"
        const val COLUMN_JSON = "json"
    }
}