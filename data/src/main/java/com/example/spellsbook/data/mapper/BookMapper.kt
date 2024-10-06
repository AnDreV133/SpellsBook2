package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.BookEntity
import com.example.spellsbook.domain.model.BookModel

fun BookModel.mapToEntity(): BookEntity =
    BookEntity(
        name = this.name
    )

fun BookEntity.mapToModel(): BookModel =
    BookModel(
        name = this.name
    ).also { it.id = this.id }

