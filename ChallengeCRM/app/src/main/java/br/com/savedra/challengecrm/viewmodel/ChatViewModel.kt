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
import android.util.Log
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel para o Chat.
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
   * 
   * CORREÇÃO: Limpa a lista atual antes de carregar o novo histórico para garantir que
   * o usuário não veja mensagens de outro chat por engano.
   */
  fun loadMessages(operatorId: String, userId: String) {
    _messages.value = emptyList() // Limpa cache local
    val chatRoomId = repository.getChatRoomId(operatorId, userId)
    currentChatRoomId = chatRoomId

    viewModelScope.launch {
      repository.getMessages(chatRoomId).collect { messageList ->
        Log.d("ChatViewModel", "Histórico carregado para $chatRoomId: ${messageList.size} msgs")
        _messages.value = messageList
      }
    }
  }

  /**
   * Carrega mensagens de um canal de segmento.
   */
  fun loadGroupMessages(segment: String) {
    _messages.value = emptyList() // Limpa cache local
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
    val message = Message(senderId = senderId, text = text, timestamp = Date(), chatRoomId = segment)
    viewModelScope.launch { 
        try {
            repository.sendGroupMessage(segment, message) 
            // Atualização otimista
            _messages.value = _messages.value + message
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Erro ao enviar msg de grupo", e)
        }
    }
  }

  /**
   * Envia uma mensagem no chat 1:1.
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
      try {
        repository.sendMessage(chatRoomId, message, roomInfo)
        // Atualização otimista
        _messages.value = _messages.value + message
      } catch (e: Exception) {
        Log.e("ChatViewModel", "Erro ao enviar msg 1:1", e)
      }
    }
  }

  fun updateMessageStatus(messageId: String, status: String) {
    viewModelScope.launch {
      repository.updateMessageStatus(messageId, status)
    }
  }
}
