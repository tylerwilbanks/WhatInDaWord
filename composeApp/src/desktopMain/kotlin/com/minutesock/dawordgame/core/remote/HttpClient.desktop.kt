package com.minutesock.dawordgame.core.remote

import io.ktor.client.engine.okhttp.OkHttp

actual fun createHttpClientEngine() = OkHttp.create()