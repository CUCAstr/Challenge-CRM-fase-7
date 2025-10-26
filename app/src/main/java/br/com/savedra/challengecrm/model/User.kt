package br.com.savedra.challengecrm.model

import java.util.Date

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val estado: String = "",
    val vip: Boolean = false,
    val memberSince: Date? = null,
    val notes: String = ""
)