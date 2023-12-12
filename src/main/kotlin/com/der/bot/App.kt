package com.der.bot

import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.message.ReactionAddEvent
import dev.kord.core.live.live
import dev.kord.core.live.on
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

private val reactionList = mutableListOf<ReactionAddEvent>()

@OptIn(KordPreview::class)
suspend fun main() {
    val kord = Kord(env("TOKEN"))

    kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read

        // ignore other bots, even ourselves. We only serve humans here!
        if (message.author?.isBot != false) return@on

        // check if our command is being invoked
        if (message.content != "!start") return@on

        // all clear, give them the pong!
        message.channel.createMessage(" ${message.channel.mention} Reacciona a este mensaje para participar").also {
            it.addReaction(ReactionEmoji.Unicode("✅"))
            it.live().on<ReactionAddEvent> { reactionEvent ->
                println(reactionEvent)
                reactionList.add(reactionEvent)
            }
        }
    }

    kord.on<MessageCreateEvent> { // runs every time a message is created that our bot can read
        // ignore other bots, even ourselves. We only serve humans here!
        if (message.author?.isBot != false) return@on

        // check if our command is being invoked
        if (message.content != "!pair") return@on

        if (reactionList.size == 1) {
            message.channel.createMessage(
                "Lo siento ${
                    reactionList.first().getUser().mention
                } no hay mas gente para emparejar :("
            )
            reactionList.clear()
            return@on
        }
        reactionList.forEach {
            println(it.getUser())
        }
        println("done!")
        reactionList.clear()
    }

    kord.login {
        // we need to specify this to receive the content of messages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
