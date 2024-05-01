package com.der.extensions.pairing

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.PublicSlashCommandContext
import com.kotlindiscord.kord.extensions.components.forms.ModalForm
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.AuditLogChangeKey
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.random.asKotlinRandom

class MatchExtension() : Extension() {
    override val name: String = "match"

    object AvailableRoles {
        val ENABLED_ROLES = mutableListOf("ADMIN", "administrador", "admin", "Admin")
    }

    override suspend fun setup() {
        publicSlashCommand {
            name = "emparejar"
            description = "Junta a la gente!"
            val matchData = mutableListOf<User>()
            val messages = mutableListOf<Message>()
            action {
                channel.messages.collect { message ->
                    messages.add(message)
                }
                val message = messages.last()
                message.getReactors(BotExtensionBuilder.EMOJI.emoji).collect {
                    if (it.isBot) {
                        matchData.add(it)
                    }
                }
                val userRoles = mutableListOf<String>()

                val user = this.member?.asUser()?.asMember(this.guild!!.id)
                user?.roles?.collect {
                    userRoles.add(it.name)
                }
                if (findEnabledRoles(userRoles) || user?.isOwner() == true) {
                    sendMessageWithPairs(matchData, this)
                } else {
                    this.channel.createMessage("No tienes permisos para ejecutar este comando shoro :(")
                }
                if (matchData.isNotEmpty()) {
                    sendMessageWithPairs(matchData, this)
                }
                edit {
                    content = "✅"
                }
            }
        }
    }

    private fun findEnabledRoles(roles: MutableList<String>): Boolean {
        var exist = false
        for (role in AvailableRoles.ENABLED_ROLES) {
            exist = roles.contains(role)
            if (exist)
                break
        }
        return exist
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
                val matches = mutableListOf<Pair<String, String>>()

                for (i in 0 until data.size - 1 step 2) {
                    val pair = data[i].mention to data[i + 1].mention
                    matches.add(pair)
                }

                if (data.size % 2 == 1) {
                    val lastPair = matches.last()
                    matches.removeAt(matches.size - 1)
                    matches.add(lastPair.first to (lastPair.second + " - " + data.last()))
                }
            }
        }

        return discordMessage
    }
}
