package com.example.spellsbook.domain.model

import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.TagEnum

data class TagFilterCollector_(
    val levels: MutableSet<LevelEnum> = mutableSetOf(),
    val schools: MutableSet<SchoolEnum> = mutableSetOf(),
) : Iterable<MutableSet<out TagEnum>> {
    fun put(tagEnum: TagEnum) {
        when (tagEnum) {
            is LevelEnum -> levels.add(tagEnum)
            is SchoolEnum -> schools.add(tagEnum)
            else -> throw IllegalArgumentException("Unknown TagEnum: $tagEnum")
        }
    }

    fun remove(tagEnum: TagEnum) {
        when (tagEnum) {
            is LevelEnum -> levels
            is SchoolEnum -> schools
            else -> throw IllegalArgumentException("Unknown TagEnum: $tagEnum")
        }.remove(tagEnum)
    }

    override fun iterator(): Iterator<MutableSet<out TagEnum>> =
        setOf(levels, schools).iterator()
}