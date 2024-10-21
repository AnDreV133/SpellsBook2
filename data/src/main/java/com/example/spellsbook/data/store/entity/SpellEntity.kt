package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.spellsbook.data.store.entity.SpellEntityConstant.COLUMN_JSON
import com.example.spellsbook.data.store.entity.SpellEntityConstant.COLUMN_LOCALE
import com.example.spellsbook.data.store.entity.SpellEntityConstant.COLUMN_NAME
import com.example.spellsbook.data.store.entity.SpellEntityConstant.COLUMN_UUID
import com.example.spellsbook.data.store.entity.SpellEntityConstant.TABLE_NAME_EN
import com.example.spellsbook.data.store.entity.SpellEntityConstant.TABLE_NAME_RU

object SpellEntityConstant {
    const val TABLE_NAME_EN = "spell_en"
    const val TABLE_NAME_RU = "spell_ru"
//    const val COLUMN_ID = "id"
    const val COLUMN_UUID = "uuid"
    const val COLUMN_LOCALE = "locale"
    const val COLUMN_NAME = "name"
    const val COLUMN_JSON = "json"
}

@Entity(
    tableName = TABLE_NAME_RU,
    primaryKeys = [
        COLUMN_UUID,
//    COLUMN_LOCALE
    ],
    foreignKeys = [
        ForeignKey(
            entity = TaggingSpellEntity::class,
            parentColumns = [TaggingSpellEntity.COLUMN_UUID],
            childColumns = [COLUMN_UUID]
        )
    ],
)
class SpellEntityRu(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = COLUMN_ID)
//    val id: Long = 0,
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
//    @ColumnInfo(name = COLUMN_LOCALE)
//    val locale: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_JSON)
    val json: String
)

@Entity(
    tableName = TABLE_NAME_EN,
    primaryKeys = [
        COLUMN_UUID,
//        COLUMN_LOCALE
    ],
    foreignKeys = [
        ForeignKey(
            entity = TaggingSpellEntity::class,
            parentColumns = [TaggingSpellEntity.COLUMN_UUID],
            childColumns = [COLUMN_UUID]
        )
    ],
)
class SpellEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = COLUMN_ID)
//    val id: Long = 0,
    @ColumnInfo(name = COLUMN_UUID)
    val uuid: String,
//    @ColumnInfo(name = COLUMN_LOCALE)
//    val locale: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_JSON)
    val json: String
)
