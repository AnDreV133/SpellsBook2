package com.example.spellsbook.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.entity.BookEntity
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import com.example.spellsbook.data.store.entity.spells.SpellEntityRu
import com.example.spellsbook.data.store.entity.tags.SchoolEntity
import com.example.spellsbook.domain.enums.SchoolEnum
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DBTest {
    private var db: AppDatabase? = null

    @Before
    fun createDb() {
        db = DBInMemory.open()
    }

    @After
    fun closeDb() {
        db?.close()
    }

    @Test
    fun getByTag_TagDao(): Unit = runBlocking {
        val tagDao = db?.schoolDao()
        val tag = SchoolEntity(
            tag = SchoolEnum.ABJURATION.toString(),
            spellUuid = "test"
        )

        tagDao?.insert(tag)
        val result = tagDao?.getByTag(SchoolEnum.ABJURATION)

        assertTrue(result != null)
        assertTrue(result?.get(0)?.spellUuid == tag.spellUuid)
        assertTrue(result?.get(0)?.tag == tag.tag)
    }

    @Test
    fun getAll_TagDao(): Unit = runBlocking {
        val tagDao = db?.schoolDao()
        val tag = SchoolEntity(
            tag = SchoolEnum.ABJURATION.toString(),
            spellUuid = "test"
        )

        tagDao?.insert(tag)
        tagDao?.insert(tag)
        val result = tagDao?.getAll()

        assertTrue(result?.size == 2)
    }

    @Test
    fun getInterval_TagDao(): Unit = runBlocking {
        val tagDao = db?.schoolDao()
        for (i in 0..<5) {
            val tag = SchoolEntity(
                tag = SchoolEnum.ABJURATION.toString(),
                spellUuid = "test$i"
            )

            tagDao?.insert(tag)
        }

        val result = tagDao?.getInterval(1, 3)

        assertTrue(result?.size == 2)
        assertTrue(result?.get(0)?.spellUuid == "test1")
        assertTrue(result?.get(1)?.spellUuid == "test2")
    }

    @Test
    fun getByBookId_BookWithSpellsDao(): Unit = runBlocking {
        val spellsListDao = db?.bookDao()
        val spellDaoEn = db?.spellDaoEn()
        val spellDaoRu = db?.spellDaoRu()
        val bookWithSpellsDao = db?.bookWithSpellsDao()

        val spellsList = BookEntity(
            name = "test"
        )
        val spell1 = SpellEntityRu(
            uuid = "test1",
            name = "test1",
            json = "{руся1}"
        )
        val spell2 = SpellEntityRu(
            uuid = "test2",
            name = "test2",
            json = "{руся2}"
        )
        val spell3 = SpellEntityEn(
            uuid = "test2",
            name = "test2",
            json = "{no rusja}"
        )

        val id = spellsListDao?.insert(spellsList)!!
        spellDaoRu?.insert(spell1)
        spellDaoRu?.insert(spell2)
        spellDaoEn?.insert(spell3)

        bookWithSpellsDao?.insert(id, spell1.uuid)
        bookWithSpellsDao?.insert(id, spell3.uuid)

        val result = bookWithSpellsDao?.getByBookId(id)

        assertTrue(result != null)
        assertTrue(result?.size == 2)
        assertTrue(result?.get(0)?.spellUuid == spell1.uuid)
        assertTrue(result?.get(1)?.spellUuid == spell2.uuid)
    }

    @Test
    fun insert_BookWithSpellsDao_duplicateValues(): Unit = runBlocking {
        val bookWithSpellsDao = db?.bookWithSpellsDao()

        val id1 = bookWithSpellsDao?.insert(1, "test")
        val id2 = bookWithSpellsDao?.insert(1, "test")

        val result = bookWithSpellsDao?.getByBookId(1)

        assertTrue(result?.size == 1)
        assertTrue(id2!! < 0)
    }

    @Test
    fun getByUuid_SpellDao(): Unit = runBlocking {
        val spellsListDao = db?.bookDao()
        val spellDaoRu = db?.spellDaoRu()
        val spellDaoEn = db?.spellDaoEn()
        val bookWithSpellsDao = db?.bookWithSpellsDao()

        val spellsList = BookEntity(
            name = "test"
        )
        val spell1ru = SpellEntityRu(
            uuid = "test1",
            name = "test1ru",
            json = "{}"
        )
        val spell2ru = SpellEntityRu(
            uuid = "test2",
            name = "test2ru",
            json = "{}"
        )
        val spell1en = SpellEntityEn(
            uuid = "test1",
            name = "test1en",
            json = "{}"
        )
        val spell2en = SpellEntityEn(
            uuid = "test2",
            name = "test2en",
            json = "{}"
        )

        val id = spellsListDao?.insert(spellsList)!!
        spellDaoRu?.insert(spell1ru)
        spellDaoRu?.insert(spell2ru)
        spellDaoEn?.insert(spell1en)
        spellDaoEn?.insert(spell2en)

        bookWithSpellsDao?.insert(id, spell1ru.uuid)
        bookWithSpellsDao?.insert(id, spell2ru.uuid)

        val resultRu = spellDaoRu?.getByUuid(listOf("test1", "test2"))
        spellDaoEn?.getByUuid(listOf("test1", "test2"))

        assertTrue(resultRu?.size == 2)
        assertTrue(resultRu?.map { it.name } == listOf("test1ru", "test2ru"))
    }
}




