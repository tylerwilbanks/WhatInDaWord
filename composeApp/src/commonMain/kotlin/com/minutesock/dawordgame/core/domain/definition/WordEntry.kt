package com.minutesock.dawordgame.core.domain.definition

import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.domain.GameLanguage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

data class WordEntry(
    override val id: Long = 0,
    val language: GameLanguage,
    val word: String,
    val fetchDate: LocalDate,
    val definitions: ImmutableList<WordDefinition>,
    val phonetic: String? = null,
    val origin: String? = null
) : DbEntity

