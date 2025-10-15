package br.com.savedra.challengecrm.model

data class User(
  val id: String,
  val name: String,
  val email: String,
  val password: String,
  val document: String,
  val role: String
)