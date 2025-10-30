package br.com.savedra.challengecrm.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
  val id: String = "",
  val senderId: String = "",
  val text: String = "",
  @ServerTimestamp
  val timestamp: Date? = null
)

data class ChatRoom(
  val id: String = "",
  val operatorId: String = "",
  val operatorName: String = "",
  val userId: String = "",
  val userName: String = "",
  val participants: List<String> = emptyList(),
  val lastMessageText: String = "",
  @ServerTimestamp
  val lastMessageTimestamp: Date? = null
)