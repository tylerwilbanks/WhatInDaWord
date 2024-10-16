package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.sqldelight.ValidWordEntity

fun ValidWordEntity.toValidWord(): ValidWord
    = ValidWord(id = id, word = word, language = GameLanguage.fromDb(language))

fun ValidWord.toValidWordEntity(): ValidWordEntity
    = ValidWordEntity(id = id, word = word, language = language.dbName)