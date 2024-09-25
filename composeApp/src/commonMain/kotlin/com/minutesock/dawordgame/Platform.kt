package com.minutesock.dawordgame

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun readFile(filename: String): String