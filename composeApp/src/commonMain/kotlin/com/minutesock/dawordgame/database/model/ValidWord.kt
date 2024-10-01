package com.minutesock.dawordgame.database.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Upsert

@Entity
data class ValidWord(
    @PrimaryKey val id: Int = 0,
    val word: String
)

@Dao
interface ValidWordDao {
    @Upsert
    suspend fun upsert(validWord: ValidWord)
}
