package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.enums.TagEnum

interface TagRepository {
    suspend fun getUuids(tag: TagEnum): List<String>
}