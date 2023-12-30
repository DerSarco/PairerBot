package com.der.extensions.pairing

import com.der.extensions.BotExtensionBuilder
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.PublicSlashCommandContext
import com.kotlindiscord.kord.extensions.components.forms.ModalForm
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.core.entity.User
import java.util.*
import kotlin.random.asKotlinRandom

class MatchExtension() : Extension() {
    override val name: String = "match"

    override suspend fun setup() {
        publicSlashCommand {
            name = "match"
            description = "Empareja a la gente "
            val matchData = mutableListOf<User>()
            action {
                channel.messages.collect { message ->
                    if (message.author?.isBot == true) {
                        message.getReactors(BotExtensionBuilder.EMOJI.emoji).collect { user ->
                            if (!user.isBot) {
                                matchData.add(user)
                            }
                        }
                    }
                }
                if (matchData.isNotEmpty()) {
                    sendMessageWithPairs(matchData, this)
                }
            }
        }
    }

    private suspend fun sendMessageWithPairs(
        data: MutableList<User>,
        publicSlashCommandContext: PublicSlashCommandContext<Arguments, ModalForm>,
    ) {
        if (data.isNotEmpty() && data.size >= 2) {
            val string = pairFunction(data)
            publicSlashCommandContext.channel.createMessage(string.toString())
        } else {
            publicSlashCommandContext.channel.createMessage("No hay suficientes participantes para emparejar :(")
        }
    }

    private fun pairFunction(data: MutableList<User>): StringBuilder {
        val discordMessage = StringBuilder()
        discordMessage.append("Los emparejamientos de esta sesión son los siguientes: \n")
        when (data.size) {
            2 -> discordMessage.append("${data.first().mention} - ${data.last().mention}")
            3 -> discordMessage.append("${data.first().mention} -${data[1].mention} -  ${data.last().mention}")
            else -> {
                data.shuffle(Random().asKotlinRandom())
                val matches = mutableListOf<Map<String, List<String>>>()
                val pairOfPeople = mutableMapOf<String, List<String>>()
                data.forEach { user ->
                    if (appendLastUsers(data, user, discordMessage)) {
                        return@forEach
                    }
                    pairOfPeople[data.indexOf(user).toString()] =
                        listOf(user.mention, data[(data.indexOf(user) + 1)].mention)
                    matches.add(pairOfPeople)
                    if (searchForExistingUser(matches, user.mention)) {
                        return@forEach
                    }
                }
                if (matches.isNotEmpty()) {
                    appendStringsOfPairs(matches, discordMessage)
                }
            }
        }

        return discordMessage
    }

    private fun appendStringsOfPairs(matches: MutableList<Map<String, List<String>>>, discordMessage: StringBuilder) {
        matches.forEach { maps ->
            val pairMessage = StringBuilder()
            maps.entries.forEach { entry ->
                entry.value.forEach {
                    pairMessage.append("$it ")
                }
                discordMessage.append("$pairMessage \n")
            }
        }
    }

    private fun searchForExistingUser(matches: MutableList<Map<String, List<String>>>, user: String): Boolean {
        matches.forEach {
            it.map { entries ->
                entries.value.forEach { mention ->
                    if (mention == user) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun appendLastUsers(
        userReactionData: MutableList<User>, it: User, string: StringBuilder
    ): Boolean {
        if (userReactionData.indexOf(it) - userReactionData.size == 2) {
            string.append(
                """
                                ${userReactionData[userReactionData.indexOf(it)]} - 
                                ${userReactionData[userReactionData.indexOf(it) + 1]}
                            """
            )
            return true
        }

        if (userReactionData.indexOf(it) - userReactionData.size == 3) {
            string.append(
                """
                                ${userReactionData[userReactionData.indexOf(it)]} - 
                                ${userReactionData[userReactionData.indexOf(it) + 1]} -
                                ${userReactionData[userReactionData.indexOf(it) + 2]} -
                            """
            )
            return true
        }
        return false
    }
}