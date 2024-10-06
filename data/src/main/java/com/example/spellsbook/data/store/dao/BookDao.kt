package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.spellsbook.data.store.entity.BookEntity


@Dao
abstract class BookDao : BaseDao<BookEntity>(BookEntity.TABLE_NAME) {
    @Query("select * from ${BookEntity.TABLE_NAME} where id = :id limit 1")
    abstract suspend fun get(id: Long): BookEntity
}