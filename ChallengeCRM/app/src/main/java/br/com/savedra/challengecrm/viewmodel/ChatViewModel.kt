package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import br.com.savedra.challengecrm.data.repository.ChatRepository
import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class ChatViewModel : ViewModel() {
  private val repository: ChatRepository = ChatRepository()

  private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
  val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms.asStateFlow()

  private val _messages = MutableStateFlow<List<Message>>(emptyList())
  val messages: StateFlow<List<Message>> = _messages.asStateFlow()


  private var currentChatRoomId: String? = null

  fun loadChatRooms(participantId: String) {
    viewModelScope.launch {
      repository.getChatRooms(participantId).collect { rooms ->
        _chatRooms.value = rooms
      }
    }
  }

  fun loadMessages(operatorId: String, userId: String) {
    val chatRoomId = repository.getChatRoomId(operatorId, userId)
    currentChatRoomId = chatRoomId

    viewModelScope.launch {
      repository.getMessages(chatRoomId).collect { messageList ->
        _messages.value = messageList
      }
    }
  }

  fun loadGroupMessages(segment: String) {
    viewModelScope.launch {
      currentChatRoomId = segment
      repository.getGroupMessages(segment).collect { messageList ->
        _messages.value = messageList
      }
    }
  }

  fun sendGroupMessage(text: String, senderId: String, segment: String) {
    if (text.isBlank()) return

    val message = Message(
      senderId = senderId,
      text = text,
      timestamp = Date()
    )

    viewModelScope.launch {
      repository.sendGroupMessage(segment, message)
    }
  }

  fun sendMessage(
    text: String,
    senderId: String,
    currentOperator: User,
    currentUser: User
  ) {
    val chatRoomId = currentChatRoomId ?: return
    if (text.isBlank()) return

    val roomInfo = ChatRoom(
      operatorId = currentOperator.id,
      operatorName = currentOperator.name,
      userId = currentUser.id,
      userName = currentUser.name
    )

    val message = Message(
      senderId = senderId,
      text = text,
      timestamp = Date()
    )

    viewModelScope.launch {
      repository.sendMessage(chatRoomId, message, roomInfo)
    }
  }

  fun markMessageAsImportant(messageId: String, isImportant: Boolean) {

    val chatRoomId = currentChatRoomId ?: return

    viewModelScope.launch {
      repository.updateMessageImportance(chatRoomId, messageId, isImportant)
    }
  }


  fun deleteMessage(messageId: String) {

    val chatRoomId = currentChatRoomId ?: return

    viewModelScope.launch {
      repository.deleteMessage(chatRoomId, messageId)
    }
  }
}

