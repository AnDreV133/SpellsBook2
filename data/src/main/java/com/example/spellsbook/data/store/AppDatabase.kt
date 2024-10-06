package com.example.spellsbook.data.store


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spellsbook.data.store.dao.BookWithSpellsDao
import com.example.spellsbook.data.store.dao.LevelDao
import com.example.spellsbook.data.store.dao.SchoolDao
import com.example.spellsbook.data.store.dao.SpellDaoEn
import com.example.spellsbook.data.store.dao.SpellDaoRu
import com.example.spellsbook.data.store.dao.BookDao
import com.example.spellsbook.data.store.entity.BookEntity
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import com.example.spellsbook.data.store.entity.spells.SpellEntityRu
import com.example.spellsbook.data.store.entity.tags.LevelEntity
import com.example.spellsbook.data.store.entity.tags.SchoolEntity

@Database(
    entities = [
        // main
        BookEntity::class,
        SpellEntityEn::class,
        SpellEntityRu::class,
        BooksSpellsXRefEntity::class,
        // tags
        LevelEntity::class,
        SchoolEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookWithSpellsDao(): BookWithSpellsDao

    // spells locales
    abstract fun spellDaoEn(): SpellDaoEn
    abstract fun spellDaoRu(): SpellDaoRu

    // tags
    abstract fun levelDao(): LevelDao
    abstract fun schoolDao(): SchoolDao
}