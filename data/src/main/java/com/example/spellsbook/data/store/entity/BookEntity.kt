package com.example.spellsbook.data.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = BookEntity.TABLE_NAME)
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String
) {
    companion object {
        const val TABLE_NAME = "book"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }
}

