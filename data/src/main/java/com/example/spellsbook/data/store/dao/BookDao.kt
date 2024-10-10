package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.spellsbook.data.store.entity.BookEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class BookDao : BaseDao<BookEntity>(BookEntity.TABLE_NAME) {
    @Query("select * from ${BookEntity.TABLE_NAME} where id = :id limit 1")
    abstract fun get(id: Long): Flow<BookEntity?>

    @Query("select * from ${BookEntity.TABLE_NAME} order by ${BookEntity.COLUMN_NAME} asc")
    abstract fun getAll(): Flow<List<BookEntity>>

}