package com.minutesock.dawordgame.core.domain.definition

import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.domain.GameLanguage

data class WordDefinition(
    override val id: Long = 0,
    val language: GameLanguage,
    val word: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String?
) : DbEntity
