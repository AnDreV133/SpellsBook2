package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.spellsbook.data.store.entity.SpellEntity.Companion.COLUMN_LANGUAGE
import com.example.spellsbook.data.store.entity.SpellEntity.Companion.COLUMN_UUID
import com.example.spellsbook.domain.enums.LocaleEnum


@Entity(
    tableName = SpellEntity.TABLE_NAME,
    primaryKeys = [COLUMN_UUID, COLUMN_LANGUAGE],
    foreignKeys = [
        ForeignKey(
            entity = TaggingSpellEntity::class,
            parentColumns = [TaggingSpellEntity.COLUMN_UUID],
            childColumns = [COLUMN_UUID]
        )
    ]
)
class SpellEntity(
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
    @ColumnInfo(name = COLUMN_LANGUAGE)
    val language: String = LocaleEnum.DEFAULT.value,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_JSON)
    val json: String
) {
    companion object {
        const val TABLE_NAME = "spell"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_LANGUAGE= "language"
        const val COLUMN_NAME = "name"
        const val COLUMN_JSON = "json"
    }
}