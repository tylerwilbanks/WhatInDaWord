package com.minutesock.dawordgame.core.remote

import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClientEngine() = Darwin.create()