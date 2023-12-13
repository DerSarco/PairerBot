package com.der.extensions

import com.der.data.MatchData
import com.der.extensions.pairing.MatchExtension
import com.der.extensions.pairing.PairExtension
import dev.kord.core.event.message.ReactionAddEvent

class BotExtensionBuilder {

    private var matchData: MatchData = MatchData(mutableListOf())

    private val liveMessage: (ReactionAddEvent) -> Unit = { reactionAddEvent ->
        matchData.list.add(reactionAddEvent)
        println("livemessage function")
        println(matchData.list)
    }

    val matchExtension = MatchExtension(matchData)
    val pairExtension = PairExtension(liveMessage)

}