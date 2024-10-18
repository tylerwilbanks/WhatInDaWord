package com.minutesock.dawordgame.core.domain

data class WordSelection(
    val id: Long = 0,
    val word: String,
    val language: GameLanguage
)