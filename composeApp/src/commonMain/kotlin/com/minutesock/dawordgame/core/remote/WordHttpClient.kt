package com.minutesock.dawordgame.core.remote

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.remote.definition.WordEntryDto
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.core.util.GeneralIssue
import com.minutesock.dawordgame.core.util.Option
import com.minutesock.dawordgame.di.KoinProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class WordHttpClient(
    private val client: HttpClient = KoinProvider.instance.get(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchWordDefinition(
        gameLanguage: GameLanguage,
        word: String
    ): Option<List<WordEntryDto>, GeneralIssue> {
        return withContext(defaultDispatcher) {
            val response = client.request {
                method = HttpMethod.Get
                url {
                    protocol = urlProtocol
                    host = Companion.host
                    appendPathSegments("api", "v2", "entries", gameLanguage.dbName, word)
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    try {
                        val wordDefinitions = response.body<List<WordEntryDto>>()
                        Option.Success(wordDefinitions)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Option.Issue(GeneralIssue(TextRes.Raw(e.message ?: "who knows"))) // todo extract
                    }
                }
                else -> {
                    Option.Issue(GeneralIssue(TextRes.Raw("Could not find word entry. (${response.status.value})"))) // todo extract
                }
            }
        }
    }

    companion object {
        val urlProtocol = URLProtocol.HTTPS
        val host = "api.dictionaryapi.dev"
        val baseUrl = "${urlProtocol.name}://$host/"
    }
}