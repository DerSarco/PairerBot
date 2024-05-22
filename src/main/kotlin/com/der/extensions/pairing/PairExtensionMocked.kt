package com.der.extensions.pairing

import com.der.helpers.findEnabledRoles
import com.der.helpers.getUserRoles
import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.edit
import java.util.*
import kotlin.random.asKotlinRandom

class PairExtensionMocked() : Extension() {

    override val name: String = "Emparejar"

    private var mutableListOfUsers = mutableListOf<String>()

    override suspend fun setup() {
        publicSlashCommand {
            name = "emparejar"
            description = "Empareja a las personas para que esto funcione!"
            action {
                val message = channel.createMessage("Empezando....")
                val messageId = message.id
                message.edit {
                    this.content = GENERIC_MESSAGE + "\n" + mutableListOfUsers.toString()
                    this.components {
                        publicButton {
                            label = "Me sumo!"
                            style = ButtonStyle.Success
                            action {
                                val mention = this.user.mention
                                if (validateIfUserExist(mention)) {
                                    return@action
                                }
                                mutableListOfUsers.add(mention)
                                edit {
                                    content = GENERIC_MESSAGE + "\n" + mutableListOfUsers.toString()
                                }
                            }
                        }
                        publicButton {
                            label = "Nope!"
                            style = ButtonStyle.Success
                            action {
                                val mention = this.user.mention
                                if (validateIfUserExist(mention)) {
                                    mutableListOfUsers.remove(mention)
                                    edit {
                                        content = GENERIC_MESSAGE + "\n" + mutableListOfUsers.toString()
                                    }
                                }
                            }
                        }
                        publicButton {
                            label = "Finalizar"
                            style = ButtonStyle.Primary
                            action {
                                val user = this.user
                                val guild = this.guild
                                if (findEnabledRoles(getUserRoles(user, guild))) {
                                    respond {
                                        content = sendMessageWithPairs(mutableListOfUsers)
                                        mutableListOfUsers.clear()
                                        channel.deleteMessage(messageId, "")
                                    }
                                } else {
                                    channel.createMessage("${user.mention} no tienes permisos para ejecutar ese comando \n No seas un pillin jiji.")
                                }
                            }
                        }
                    }
                }.also {
                    edit {
                        this.content = "Comando ejecutado!"
                    }
                }
            }
        }
    }

    private fun validateIfUserExist(user: String): Boolean {
        val exist = mutableListOfUsers.find { user == it }
        return exist != null
    }

    companion object {
        const val GENERIC_MESSAGE = "Vas a participar de la siguiente actividad?"
    }

    private fun sendMessageWithPairs(
        data: MutableList<String>
    ): String = if (data.isNotEmpty() && data.size >= 2) {
        pairFunction(data).toString()
    } else {
        "No hay suficientes participantes para emparejar :("
    }


    private fun pairFunction(data: MutableList<String>): StringBuilder {
        val discordMessage = StringBuilder()
        discordMessage.append("## Los emparejamientos de esta sesión son los siguientes: \n")
        when (data.size) {
            2 -> discordMessage.append("### ${data.first()} - ${data.last()}")
            3 -> discordMessage.append("### ${data.first()} - ${data[1]} -  ${data.last()}")
            else -> {
                data.shuffle(Random().asKotlinRandom())
                val matches = mutableListOf<Pair<String, String>>()

                for (i in 0 until data.size - 1 step 2) {
                    val pair = data[i] to data[i + 1]
                    matches.add(pair)
                }

                if (data.size % 2 == 1) {
                    val lastPair = matches.last()
                    matches.removeAt(matches.size - 1)
                    matches.add(lastPair.first to (lastPair.second + " - " + data.last()))
                }

                for (i in matches) {
                    discordMessage.append("### ${i.first} + ${i.second}")
                    discordMessage.append("\n")
                }
            }
        }

        return discordMessage
    }

}
