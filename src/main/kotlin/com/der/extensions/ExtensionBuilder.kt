package com.der.extensions

import com.der.extensions.pairing.MatchExtension
import com.der.extensions.pairing.PairExtension
import dev.kord.core.entity.ReactionEmoji

class BotExtensionBuilder {

    object EMOJI {
        val emoji = ReactionEmoji.Unicode("✅")
    }

    val matchExtension = MatchExtension()
    val pairExtension = PairExtension()

}