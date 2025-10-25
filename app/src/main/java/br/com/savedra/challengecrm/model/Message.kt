package br.com.savedra.challengecrm.model

import java.util.Date

data class Message(
  val id: String,
  val content: String,
  val sender: String,
  val subject: String,
  val timestamp: Date,
  val type: MessageType, // MESSAGE ou CAMPAIGN
  val isRead: Boolean = false
)

enum class MessageType {
  MESSAGE,
  CAMPAIGN
}