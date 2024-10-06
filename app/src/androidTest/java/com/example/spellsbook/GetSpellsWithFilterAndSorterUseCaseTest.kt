package com.example.spellsbook

import com.example.spellsbook.data.store.dao.LevelDao
import com.example.spellsbook.data.store.dao.SchoolDao
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import javax.inject.Inject

@HiltAndroidTest
class GetSpellsWithFilterAndSorterUseCaseTest {
    @Inject
    lateinit var useCase: GetSpellsWithFilterAndSorterUseCase

    @Inject
    lateinit var levelDao: LevelDao

    @Inject
    lateinit var schoolDao: SchoolDao

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun initUseCase() {
        hiltRule.inject()
//        sleep(8000) // todo: db preparation not finishing before execute
    }

    @Test
    fun test_execute_on_filter(): Unit = runBlocking {
        delay(8000)

        println(levelDao.getAll())
        println(schoolDao.getAll())

        val execute = useCase.execute(
            filter = listOf(
                listOf(
                    LevelEnum.LEVEL_1,
                    LevelEnum.LEVEL_2
                ),
                listOf(
                    SchoolEnum.ABJURATION,
                    SchoolEnum.CONJURATION,
                    SchoolEnum.DIVINATION
                )
            ),
        )

        println(execute.size)
        println(execute.filter {
            (
                    it.level == 1
                            || it.level == 2
                    ) && (
                    it.school == "Abjuration"
                            || it.school == "Conjuration"
                            || it.school == "Divination"
                    )
        }.size)
        println(execute)

        assertTrue(execute.isNotEmpty())
        assertTrue(
            execute.size == execute.filter {
                (
                        it.level == 1
                                || it.level == 2
                        ) && (
                        it.school == "Abjuration"
                                || it.school == "Conjuration"
                                || it.school == "Divination"
                        )
            }.size
        )
    }

    @Test
    fun test_execute_on_filter_with_one_category(): Unit = runBlocking {
        delay(8000)

        println(levelDao.getAll())
        println(schoolDao.getAll())

        val execute = useCase.execute(
            filter = listOf(
                listOf(
                    LevelEnum.LEVEL_1,
                    LevelEnum.LEVEL_2
                )
            ),
        )

        println(execute)

        assertTrue(execute.isNotEmpty())
        assertTrue(
            execute.size == execute.filter {
                it.level == 1 || it.level == 2
            }.size
        )
    }

    @Test
    fun test_execute_on_filter_with_one_category_one_position(): Unit = runBlocking {
        delay(8000)

        println(levelDao.getAll())
        println(schoolDao.getAll())

        val execute = useCase.execute(
            filter = listOf(
                listOf(
                    LevelEnum.LEVEL_1
                )
            ),
        )

        println(execute)
        println(execute.size)

        assertTrue(execute.isNotEmpty())
        assertTrue(
            execute.size == execute.filter {
                it.level == 1
            }.size
        )
    }
}