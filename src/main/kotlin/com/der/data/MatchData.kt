package com.der.data

import dev.kord.core.event.message.ReactionAddEvent

data class MatchData(
    val list: MutableList<ReactionAddEvent>
){
    fun getData() = list
}
