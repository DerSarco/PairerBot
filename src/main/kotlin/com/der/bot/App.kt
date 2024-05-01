package com.der.bot

import BotExtensionBuilder
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import dev.kord.common.annotation.KordPreview

@OptIn(KordPreview::class)
suspend fun main() {
    val extensionBuilder = BotExtensionBuilder()

    val kord = ExtensibleBot(env("TOKEN")) {
        extensions {
            add(extensionBuilder::pairExtension)
            add(extensionBuilder::matchExtension)
        }
    }

    kord.start()
}
