package com.minutesock.dawordgame.core.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidWordsDto(
    @SerialName("valid_words")
    val words: List<String>
)

@Serializable
data class WordSelectionDto(
    @SerialName("word_selection")
    val words: List<String>
)