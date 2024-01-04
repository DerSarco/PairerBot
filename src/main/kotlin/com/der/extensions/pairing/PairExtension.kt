package com.der.extensions.pairing

import com.der.extensions.BotExtensionBuilder
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand

class PairExtension() : Extension() {

    override val name: String = "pair"

    override suspend fun setup() {
        publicSlashCommand {
            name = "pair"
            description = "Pair people to make networking or pair programming!"
            action {
                this.channel.createMessage("${channel.mention} reacciona a este mensaje si deseas participar").also {
                    it.addReaction(BotExtensionBuilder.EMOJI.emoji)
                    edit {
                        content = "✅"
                    }
                }
            }
        }
    }
}