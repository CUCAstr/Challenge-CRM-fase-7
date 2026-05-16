package br.com.savedra.challengecrm.model

import java.util.Date

data class Message(
  var id: String = "",
  var chatRoomId: String = "",
  val senderId: String = "",
  val text: String = "",
  val timestamp: Date? = null,
  var isImportant: Boolean = false,
  var status: String = "SENT" // SENT, DELIVERED, READ, FAILED
) {

  constructor() : this("", "", "", "", null, false, "SENT")
}

data class ChatRoom(
  var id: String = "",
  val operatorId: String = "",
  val operatorName: String = "",
  val userId: String = "",
  val userName: String = "",
  val participants: List<String> = emptyList(),
  val lastMessageText: String = "",
  val lastMessageTimestamp: Date? = null
) {

  constructor() : this("", "", "", "", "", emptyList(), "", null)
}
