package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = BooksSpellsXRefEntity.TABLE_NAME,
    primaryKeys = [
        BooksSpellsXRefEntity.COLUMN_SPELLS_LIST_ID,
        BooksSpellsXRefEntity.COLUMN_SPELL_UUID
    ]
)
class BooksSpellsXRefEntity(
    @ColumnInfo(name = COLUMN_SPELLS_LIST_ID)
    val bookId: Long,
    @ColumnInfo(name = COLUMN_SPELL_UUID)
    val spellUuid: String
) {
    companion object {
        const val TABLE_NAME = "books_with_spells"
        const val COLUMN_SPELLS_LIST_ID = "book_id"
        const val COLUMN_SPELL_UUID = "spell_uuid"
    }
}