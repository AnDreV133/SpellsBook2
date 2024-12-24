package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BooksWithSpellsDao {
    @Query(
        """
        delete from ${BooksSpellsXRefEntity.TABLE_NAME} 
            where ${BooksSpellsXRefEntity.COLUMN_BOOK_ID} = :bookId
                and ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID} = :spellUuid
        """
    )
    abstract suspend fun delete(bookId: Long, spellUuid: String): Int

    @Query(
        """
        insert or replace into ${BooksSpellsXRefEntity.TABLE_NAME} 
            (${BooksSpellsXRefEntity.COLUMN_BOOK_ID}, ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID})
            values (:bookId, :spellUuid)
        """
    )
    abstract suspend fun insert(bookId: Long, spellUuid: String): Long

    @Query(
        """
        select ${BooksSpellsXRefEntity.COLUMN_SPELL_UUID} 
            from ${BooksSpellsXRefEntity.TABLE_NAME}
            where ${BooksSpellsXRefEntity.COLUMN_BOOK_ID}=:bookId
        """
    )
    abstract suspend fun getByBookId(bookId: Long): List<String>
}