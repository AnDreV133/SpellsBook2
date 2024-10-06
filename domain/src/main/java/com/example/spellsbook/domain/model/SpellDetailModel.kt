package com.example.spellsbook.domain.model

data class SpellDetailModel(
    val uuid: String,
    val name: String,
    val level: Int,
    val school: String,
    val description: String,
    val components: String,
    val materials: String,
    val duration: String,
    val range: String,
    val castingTime: String,
    val source: String,
)