package br.com.savedra.challengecrm.model

data class Invite(
    val name: String = "",
    val description: String = "",
    val eventDate: String = "",
    val location: String = ""
)

data class Campaign(
    val name: String = "",
    val description: String = "",
    val startDate: String = "",
    val endDate: String = ""
)

data class Message(
    val title: String = "",
    val message: String = ""
)
