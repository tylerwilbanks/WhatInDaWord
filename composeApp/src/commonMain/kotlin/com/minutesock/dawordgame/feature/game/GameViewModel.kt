package com.minutesock.dawordgame.feature.game

import androidx.lifecycle.ViewModel
import com.minutesock.dawordgame.core.data.ValidWordDataSource

class GameViewModel(
    private val validWordDataSource: ValidWordDataSource
) : ViewModel() {

    init {
        println("I am a created view model!")
        println("here is my datasource: $validWordDataSource")
    }
}