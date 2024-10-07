package com.minutesock.dawordgame.database.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity
data class ValidWord(
    @PrimaryKey val id: Int = 0,
    val word: String,
    val language: String
)

@Dao
interface ValidWordDao {
    @Query("DELETE FROM ValidWord")
    suspend fun nukeTable()

    @Upsert
    suspend fun upsert(validWords: List<ValidWord>)

    @Query("SELECT COUNT(*) from ValidWord")
    suspend fun getValidWordCount(): Int
}
