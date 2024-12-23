package com.example.spellsbook.domain.enums

enum class LevelEnum : TagEnum {
    LEVEL_0,
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4,
    LEVEL_5,
    LEVEL_6,
    LEVEL_7,
    LEVEL_8,
    LEVEL_9;
}

fun LevelEnum?.toDigit() = when (this) {
    LevelEnum.LEVEL_0 -> 0
    LevelEnum.LEVEL_1 -> 1
    LevelEnum.LEVEL_2 -> 2
    LevelEnum.LEVEL_3 -> 3
    LevelEnum.LEVEL_4 -> 4
    LevelEnum.LEVEL_5 -> 5
    LevelEnum.LEVEL_6 -> 6
    LevelEnum.LEVEL_7 -> 7
    LevelEnum.LEVEL_8 -> 8
    LevelEnum.LEVEL_9 -> 9
    else -> null
}