package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity

@Dao
abstract class BookWithSpellsDao {
    @Query(
        """
        select * from ${BooksSpellsXRefEntity.TABLE_NAME}
        where ${BooksSpellsXRefEntity.COLUMN_SPELLS_LIST_ID} = :bookId
        """
    )
    abstract suspend fun getByBookId(bookId: Long): List<BooksSpellsXRefEntity>

    @Query(
        """
        delete from ${BooksSpellsXRefEntity.TABLE_NAME} 
        where ${BooksSpellsXRefEntity.COLUMN_SPELLS_LIST_ID} = :bookId
        and ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID} = :spellUuid
        """
    )
    abstract suspend fun delete(bookId: Long, spellUuid: String)

    @Query(
        """
        insert into ${BooksSpellsXRefEntity.TABLE_NAME} 
        (${BooksSpellsXRefEntity.COLUMN_SPELLS_LIST_ID}, ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID})
        select :bookId, :spellUuid where not exists (
            select 1 FROM ${BooksSpellsXRefEntity.TABLE_NAME}  
            where ${BooksSpellsXRefEntity.COLUMN_SPELLS_LIST_ID} = :bookId
            and ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID} = :spellUuid
        )
        """
    )
    abstract suspend fun insert(bookId: Long, spellUuid: String): Long
}