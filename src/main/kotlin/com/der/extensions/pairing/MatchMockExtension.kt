package com.der.extensions.pairing

import com.der.helpers.appendStringsOfPairs
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.PublicSlashCommandContext
import com.kotlindiscord.kord.extensions.components.forms.ModalForm
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import java.util.*
import kotlin.random.asKotlinRandom

class MatchMockExtension() : Extension() {
    override val name: String = "matchMock"

    override suspend fun setup() {
        publicSlashCommand {
            name = "matchMock"
            description = "Empareja a la gente"
            val matchData = mutableListOf("CARLOS", "MIGUEL", "JOSELITO", "PEDRO", "JULIAN", "ESTEBAN", "ANA")
            action {
                sendMessageWithPairs(matchData, this)
                edit {
                    content = "✅"
                }
            }
        }
    }


    private suspend fun sendMessageWithPairs(
        data: MutableList<String>,
        publicSlashCommandContext: PublicSlashCommandContext<Arguments, ModalForm>,
    ) {
        if (data.isNotEmpty() && data.size >= 2) {
            val string = pairFunction(data)
            publicSlashCommandContext.channel.createMessage(string.toString())
        } else {
            publicSlashCommandContext.channel.createMessage("No hay suficientes participantes para emparejar :(")
        }
    }

    private fun pairFunction(data: MutableList<String>): StringBuilder {
        val discordMessage = StringBuilder()
        discordMessage.append("# Los emparejamientos de esta sesión son los siguientes: \n")
        when (data.size) {
            2 -> discordMessage.append("${data.first()} - ${data.last()}")
            3 -> discordMessage.append("${data.first()} -${data[1]} -  ${data.last()}")
            else -> {
                data.shuffle(Random().asKotlinRandom())
                val matches = mutableListOf<Pair<String, String>>()

                // Emparejar las personas en pares
                for (i in 0 until data.size - 1 step 2) {
                    val pair = data[i] to data[i + 1]
                    matches.add(pair)
                }

                if (data.size % 2 == 1) {
                    val lastPair = matches.last()
                    matches.removeAt(matches.size - 1)
                    matches.add(lastPair.first to (lastPair.second + " - " + data.last()))
                }

                appendStringsOfPairs(matches = matches, discordMessage)
            }
        }

        return discordMessage
    }

}