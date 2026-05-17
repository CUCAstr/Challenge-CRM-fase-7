package br.com.savedra.challengecrm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.savedra.challengecrm.data.repository.ChatRepository
import br.com.savedra.challengecrm.di.RepositoryProvider
import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel para o Chat.
 * 
 * CORREÇÃO: Tratamento de nulos em todos os campos de usuário para evitar crashes.
 */
class ChatViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ChatRepository = RepositoryProvider.getChatRepository(application)

  private val _chatRooms = MutableStateFlow<List<ChatRoom>>(emptyList())
  val chatRooms: StateFlow<List<ChatRoom>> = _chatRooms.asStateFlow()

  private val _messages = MutableStateFlow<List<Message>>(emptyList())
  val messages: StateFlow<List<Message>> = _messages.asStateFlow()

  private var currentChatRoomId: String? = null

  /**
   * Carrega salas de chat do usuário.
   */
  fun loadChatRooms(participantId: String) {
    viewModelScope.launch {
      repository.getChatRooms(participantId).collect { rooms ->
        _chatRooms.value = rooms
      }
    }
  }

  /**
   * Carrega mensagens entre um operador e um cliente.
   */
  fun loadMessages(operatorId: String, userId: String) {
    val chatRoomId = repository.getChatRoomId(operatorId, userId)
    currentChatRoomId = chatRoomId

    viewModelScope.launch {
      repository.getMessages(chatRoomId).collect { messageList ->
        _messages.value = messageList
      }
    }
  }

  /**
   * Carrega mensagens de um canal de segmento.
   */
  fun loadGroupMessages(segment: String) {
    viewModelScope.launch {
      currentChatRoomId = segment
      repository.getGroupMessages(segment).collect { messageList ->
        _messages.value = messageList
      }
    }
  }

  /**
   * Envia uma mensagem em um canal de segmento.
   */
  fun sendGroupMessage(text: String, senderId: String, segment: String) {
    if (text.isBlank()) return
    val message = Message(senderId = senderId, text = text, timestamp = Date())
    viewModelScope.launch { repository.sendGroupMessage(segment, message) }
  }

  /**
   * Envia uma mensagem no chat 1:1.
   * 
   * CORREÇÃO: Usando Elvis operator (?: "") para garantir que campos opcionais do User
   * não causem erro de tipo ao criar objetos do modelo de Chat.
   */
  fun sendMessage(
    text: String,
    senderId: String,
    currentOperator: User,
    currentUser: User
  ) {
    val chatRoomId = currentChatRoomId ?: return
    if (text.isBlank()) return

    val roomInfo = ChatRoom(
      operatorId = currentOperator.id ?: "",
      operatorName = currentOperator.name ?: "",
      userId = currentUser.id ?: "",
      userName = currentUser.name ?: ""
    )

    val message = Message(
      senderId = senderId,
      text = text,
      timestamp = Date(),
      chatRoomId = chatRoomId
    )

    viewModelScope.launch {
      repository.sendMessage(chatRoomId, message, roomInfo)
    }
  }

  fun updateMessageStatus(messageId: String, status: String) {
    viewModelScope.launch {
      repository.updateMessageStatus(messageId, status)
    }
  }
}
