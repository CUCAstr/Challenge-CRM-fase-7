package br.com.savedra.challengecrm.model

import java.util.Date

data class User(
  val id: String = "",
  val name: String = "",
  val company: String = "",
  val email: String = "",
  val role: String = "",
  val segment: String = "",
  val score: Int = 0,
  val status: String = "",
  val memberSince: Date? = null,
  val notes: String = "",
  val gender: String = "",
  val phone: String = "",
  val category: String = ""
)