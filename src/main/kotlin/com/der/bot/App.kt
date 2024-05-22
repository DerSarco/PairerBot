package com.der.bot

import BotExtensionBuilder
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env

suspend fun main() {
    val extensionBuilder = BotExtensionBuilder()

    val kord = ExtensibleBot(env("TOKEN")) {
        extensions {
            add(extensionBuilder::pairExtensionMocked)
        }
    }

    kord.start()
}
