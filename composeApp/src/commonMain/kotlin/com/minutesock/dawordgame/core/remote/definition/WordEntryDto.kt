package com.minutesock.dawordgame.core.remote.definition

import kotlinx.serialization.Serializable

@Serializable
data class WordEntryDto(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<PhoneticDto>,
    val meanings: List<MeaningDto>,
    val license: LicenseDto? = null,
    val sourceUrls: List<String>? = null
)

@Serializable
data class LicenseDto(
    val name: String,
    val url: String
)

@Serializable
data class PhoneticDto(
    val text: String? = null,
    val audio: String? = null,
    val sourceUrl: String? = null,
    val license: LicenseDto? = null
)

@Serializable
data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)

@Serializable
data class DefinitionDto(
    val definition: String,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val example: String? = null
)