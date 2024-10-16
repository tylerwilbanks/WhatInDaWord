package com.minutesock.dawordgame.core.domain

data class ValidWord(
    val id: Long = 0,
    val word: String,
    val language: GameLanguage
)

interface ValidWordDao {
//    @Query("DELETE FROM ValidWord")
    suspend fun nukeTable()

//    @Upsert
    suspend fun upsert(validWords: List<ValidWord>)

//    @Query("SELECT COUNT(*) from ValidWord")
    suspend fun getValidWordCount(): Int
}
