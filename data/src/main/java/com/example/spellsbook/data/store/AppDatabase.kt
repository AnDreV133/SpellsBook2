package com.example.spellsbook.data.store


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spellsbook.data.store.dao.BookDao
import com.example.spellsbook.data.store.dao.BookWithSpellsDao
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.data.store.dao.TaggingSpellDao
import com.example.spellsbook.data.store.entity.BookEntity
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.SpellEntityRu
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

@Database(
    entities = [
        BookEntity::class,
        BooksSpellsXRefEntity::class,
        TaggingSpellEntity::class,
        SpellEntity::class,
        SpellEntityRu::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun bookWithSpellsDao(): BookWithSpellsDao

    abstract fun spellDao(): SpellDao

    abstract fun taggingSpellDao(): TaggingSpellDao
}