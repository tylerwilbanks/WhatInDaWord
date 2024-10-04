package com.minutesock.dawordgame.feature.game

import androidx.lifecycle.ViewModel
import com.minutesock.dawordgame.database.AppDatabase

class GameViewModel(
    private val database: AppDatabase
) : ViewModel() {

    init {
        println("I am a created view model!")
        println("here is my database! $database")
    }
}