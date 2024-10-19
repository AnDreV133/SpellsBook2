package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spellsbook.data.store.entity.BookEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class BookDao {
    @Query("select * from ${BookEntity.TABLE_NAME} where id = :id limit 1")
    abstract fun get(id: Long): Flow<BookEntity?>

    @Query("select * from ${BookEntity.TABLE_NAME} order by ${BookEntity.COLUMN_NAME} asc")
    abstract fun getAll(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: BookEntity): Long

    @Query("delete from ${BookEntity.TABLE_NAME} where id = :id")
    abstract suspend fun delete(id: Long): Int
}