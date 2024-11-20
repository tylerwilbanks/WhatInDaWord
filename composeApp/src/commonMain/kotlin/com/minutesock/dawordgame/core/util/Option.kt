package com.minutesock.dawordgame.core.util

import com.minutesock.dawordgame.core.uiutil.TextRes

sealed class Option<out D, out E> {
    class Issue<out E : IssueInfo>(val issue: E) : Option<Nothing, E>()
    class Success<out D>(val data: D) : Option<D, Nothing>()
}

sealed class ContinuousOption<out D, out E> {
    class Loading<out D>(
        val data: D,
        val continuousStatus: ContinuousStatus
    ) : ContinuousOption<D, Nothing>()

    class Issue<out D, out E : IssueInfo>(val issue: E, val data: D? = null) : ContinuousOption<D, E>()
    class Success<out D>(val data: D) : ContinuousOption<D, Nothing>()
}

interface IssueInfo {
    val textRes: TextRes
    val errorCode: Int
}

class GeneralIssue(
    override val textRes: TextRes,
    override val errorCode: Int = 1_000
) : IssueInfo

inline fun <D, E> Option<D, E>.onSuccess(successBlock: (data: D) -> Unit) {
    when (this) {
        is Option.Issue -> Unit
        is Option.Success -> successBlock(data)
    }
}

inline fun <D, E> ContinuousOption<D, E>.onSuccess(successBlock: (data: D) -> Unit) {
    when (this) {
        is ContinuousOption.Loading -> Unit
        is ContinuousOption.Issue -> Unit
        is ContinuousOption.Success -> successBlock(data)
    }
}

sealed class ContinuousStatus {
    data class Indefinite(val textRes: TextRes) : ContinuousStatus() {
        companion object {
            fun empty(): ContinuousStatus.Indefinite {
                return ContinuousStatus.Indefinite(TextRes.empty())
            }
        }
    }
    data class Progress(
        val textRes: TextRes,
        val currentProgress: Double = 0.0,
        val maxProgress: Double = 1.0
    ) : ContinuousStatus() {
        val displayPercentage: Int get() = (currentProgress / maxProgress).toInt() * 100
    }
}
