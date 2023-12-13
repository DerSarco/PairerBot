package com.der.extensions.pairing

import com.der.data.MatchData
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import java.lang.StringBuilder

class MatchExtension(
    private val matchData: MatchData
): Extension() {
    override val name: String = "match"

    override suspend fun setup() {
        publicSlashCommand {
            name = "match"
            description = "Empareja a la gente "
            println(matchData.getData())
            if (matchData.getData().isEmpty()){
                action {
                    respond {
                        content = "${user.mention} Lo siento, no hay personas con las cuales emparejar :("
                    }
                    matchData.list.clear()
                }
                return@publicSlashCommand
            }
            action {
                val message = StringBuilder()
                matchData.getData().forEach() {
                    message.append(it.getUser().mention)
                }
                respond {
                    content = message.toString()
                    matchData.list.clear()
                }
            }
        }
    }
}