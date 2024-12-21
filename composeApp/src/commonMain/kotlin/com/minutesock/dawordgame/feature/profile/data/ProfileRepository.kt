package com.minutesock.dawordgame.feature.profile.data

import com.minutesock.dawordgame.core.data.source.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GuessDistributionItem
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.profile.presentation.GuessDistributionState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProfileRepository(
    private val wordSessionDataSource: WordSessionDataSource = KoinProvider.instance.get(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getGuessDistribution(language: GameLanguage, maxGuessAttempts: Int = 6) = flow {
        var guessDistributionState = GuessDistributionState(
            loading = true
        )
        emit(guessDistributionState)
        val finalDistribution = MutableList(maxGuessAttempts) { index ->
            GuessDistributionItem(
                attemptCount = index.toLong() + 1,
                wordSessionCount = 0
            )
        }
        val guessDistributionFromDb = wordSessionDataSource.selectGuessDistribution(language)
        guessDistributionFromDb.forEach { dbItem ->
            finalDistribution.forEachIndexed { index, finalItem ->
                if (finalItem.attemptCount == dbItem.attemptCount) {
                    finalDistribution[index] = finalItem.copy(wordSessionCount = dbItem.wordSessionCount)
                }
            }
        }
        guessDistributionState = guessDistributionState.copy(
            guessDistributions = finalDistribution.toImmutableList()
        )
        val failedSessionsCount = wordSessionDataSource.selectFailedSessionsCount(language)
        guessDistributionState = guessDistributionState.copy(
            failedGameSessionsCount = failedSessionsCount,
            totalSessionCount = maxOf(
                guessDistributionState.guessDistributions.maxOf { it.wordSessionCount },
                failedSessionsCount
            ),
            loading = false
        )
        emit(guessDistributionState)
    }.flowOn(defaultDispatcher)
}