package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.database.getDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::getDatabase)
}