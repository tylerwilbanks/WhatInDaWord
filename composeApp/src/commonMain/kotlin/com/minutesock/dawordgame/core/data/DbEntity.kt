package com.minutesock.dawordgame.core.data

interface DbEntity {
    val id: Long
    val idForDbInsertion get() = resolveIdForDbInsertion()

    private fun resolveIdForDbInsertion(): Long? {
        return if (id == 0L) {
            null
        } else {
            id
        }
    }
}