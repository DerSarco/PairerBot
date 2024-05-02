package com.der.extensions.pairing

import BotExtensionBuilder
import com.der.helpers.findEnabledRoles
import com.der.helpers.getUserRoles
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand

class PairExtension() : Extension() {

    override val name: String = "pair"

    override suspend fun setup() {
        publicSlashCommand {
            name = "empezar"
            description = "Empareja a las personas para que esto funcione!"
            action {
                val userRoles = getUserRoles()
                if (findEnabledRoles(userRoles)) {
                    this.channel.createMessage("${channel.mention} reacciona a este mensaje si deseas participar")
                        .also {
                            it.addReaction(BotExtensionBuilder.EMOJI.emoji)
                            edit {
                                content = "✅"
                            }
                        }
                } else {
                    this.channel.createMessage("No tienes permisos para ejecutar este comando :(")
                    edit {
                        content = "❌"
                    }
                }
            }
        }
    }
}
