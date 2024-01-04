package com.der.helpers

fun appendStringsOfPairs(matches: MutableList<Pair<String, String>>, discordMessage: StringBuilder) {
    matches.forEach { maps ->
        val pairMessage = StringBuilder()
        pairMessage.append("## ${maps.first} - ${maps.second} ")
        discordMessage.append("$pairMessage \n")
    }
}