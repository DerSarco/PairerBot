package com.der.helpers

import BotExtensionBuilder
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.UserBehavior

fun findEnabledRoles(roles: MutableList<String>): Boolean {
    var exist = false
    for (role in BotExtensionBuilder.AvailableRoles.ENABLED_ROLES) {
        exist = roles.contains(role)
        if (exist)
            break
    }
    return exist
}


suspend fun getUserRoles(user: UserBehavior, guild: GuildBehavior?): MutableList<String>{
    val userRoles: MutableList<String> = mutableListOf()
    if (guild != null){
        val userGiven = user.asMemberOrNull(guild.id)
        userGiven?.roles?.collect {
            userRoles.add(it.name)
        }
    }
    return userRoles
}