package com.minutesock.dawordgame.core.domain

import com.minutesock.dawordgame.core.data.DbEntity

data class ValidWord(
    override val id: Long = 0,
    val word: String,
    val language: GameLanguage
) : DbEntity
