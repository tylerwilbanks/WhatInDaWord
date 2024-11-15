package com.minutesock.dawordgame.feature.game.remote

import com.minutesock.dawordgame.di.KoinProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class WordHttpClient(private val client: HttpClient = KoinProvider.instance.get()) {

    suspend fun fetchWordDefinition(word: String) { // todo Option<WordDefinition, NetworkError>
        val response = client.request(BASE_URL) {
            method = HttpMethod.Get
            url {
                protocol = URLProtocol.HTTPS
                host = "api.dictionaryapi.dev"
                appendPathSegments("api", "v2")
                parameters.append("word", word)
            }
        }
    }

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"
    }
}