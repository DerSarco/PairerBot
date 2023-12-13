package com.der.extensions.pairing

import com.kotlindiscord.kord.extensions.extensions.*
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.live.live
import dev.kord.core.live.on

class PairExtension(
    private var liveMessage: (ReactionAddEvent) -> Unit,
) : Extension() {

    override val name: String = "pair"

    @OptIn(KordPreview::class)
    override suspend fun setup() {
        publicSlashCommand {
            name = "pair"
            description = "Pair people to make networking or pair programming!"
            action {
                this.channel.createMessage("${channel.mention} reacciona a este mensaje si deseas participar").also {
                    it.addReaction(ReactionEmoji.Unicode("✅"))
                    it.live().on<ReactionAddEvent> { reactionAddEvent ->
                        liveMessage(reactionAddEvent)
                        println("added")
                    }
                }
            }
        }
    }
}