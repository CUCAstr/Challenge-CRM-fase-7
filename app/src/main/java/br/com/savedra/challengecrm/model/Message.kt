package br.com.savedra.challengecrm.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Message(
  val content: String,
  val sender: String,
  val date: LocalDate,
  val time: LocalDateTime
)