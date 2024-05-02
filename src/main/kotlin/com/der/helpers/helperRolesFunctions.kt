package com.der.helpers

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.PublicSlashCommandContext
import com.kotlindiscord.kord.extensions.components.forms.ModalForm

fun findEnabledRoles(roles: MutableList<String>): Boolean {
    var exist = false
    for (role in BotExtensionBuilder.AvailableRoles.ENABLED_ROLES) {
        exist = roles.contains(role)
        if (exist)
            break
    }
    return exist
}


suspend fun PublicSlashCommandContext<Arguments, ModalForm>.getUserRoles(): MutableList<String> {
    val userRoles: MutableList<String> = mutableListOf()
    val user = this.member?.asUser()?.asMember(this.guild!!.id)
    user?.roles?.collect {
        userRoles.add(it.name)
    }
    return userRoles
}