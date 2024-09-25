package com.minutesock.dawordgame

import java.io.File

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun readFile(filename: String): String {
    return File("src/commonMain/composeResources/files/$filename").readText()
}