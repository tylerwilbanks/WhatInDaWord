package com.minutesock.dawordgame.core.util

import com.minutesock.dawordgame.core.uiutil.TextRes

sealed class Option<out D, out E> {
    class Issue<out E : IssueInfo>(val issue: E) : Option<Nothing, E>()
    class Success<out D>(val data: D) : Option<D, Nothing>()
}

sealed class ContinuousOption<out D, out E> {
    class Loading(
        val textRes: TextRes,
    ) : ContinuousOption<Nothing, Nothing>()

    class Issue<out E> : ContinuousOption<Nothing, E>()
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