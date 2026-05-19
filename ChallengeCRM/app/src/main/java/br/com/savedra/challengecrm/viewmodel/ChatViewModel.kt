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
import android.widget.Toast
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

  // Contador de mensagens não lidas por sala (Útil para testes solo)
  private val _unreadCount = MutableStateFlow<Map<String, Int>>(emptyMap())
  val unreadCount: StateFlow<Map<String, Int>> = _unreadCount.asStateFlow()

  private var currentChatRoomId: String? = null
  private var loadMessagesJob: kotlinx.coroutines.Job? = null

  /**
   * Limpa o estado atual do chat.
   */
  private fun clearChatState() {
      Log.d("ChatViewModel", "Limpando estado do chat...")
      loadMessagesJob?.cancel()
      _messages.value = emptyList()
      currentChatRoomId = null
  }

  /**
   * Carrega salas de chat do usuário e calcula mensagens não lidas.
   */
  fun loadChatRooms(participantId: String) {
    viewModelScope.launch {
      repository.getChatRooms(participantId).collect { rooms ->
        _chatRooms.value = rooms
        calculateUnreadGlobal(rooms, participantId)
      }
    }
  }

  private fun calculateUnreadGlobal(rooms: List<ChatRoom>, currentUserId: String) {
      val newCounts = mutableMapOf<String, Int>()
      rooms.forEach { room ->
          viewModelScope.launch {
              repository.getMessages(room.id).collect { msgs ->
                  val unread = msgs.count { it.senderId != currentUserId && it.status != "READ" }
                  newCounts[room.id] = unread
                  _unreadCount.value = _unreadCount.value + (room.id to unread)
              }
          }
      }
  }

  /**
   * Carrega mensagens entre um operador e um cliente.
   */
  fun loadMessages(operator: User, user: User, currentSenderId: String) {
    val operatorId = operator.id?.trim() ?: "unknown_op"
    val userId = user.id?.trim() ?: "unknown_user"
    val chatRoomId = repository.getChatRoomId(operatorId, userId)
    
    if (currentChatRoomId == chatRoomId && loadMessagesJob?.isActive == true) {
        // Forçar marcação de leitura mesmo se já estiver aberto
        viewModelScope.launch { repository.markAsRead(chatRoomId, currentSenderId) }
        return
    }

    Log.d("ChatViewModel", "loadMessages INICIANDO: $chatRoomId")
    
    loadMessagesJob?.cancel()
    _messages.value = emptyList()
    currentChatRoomId = chatRoomId
    
    loadMessagesJob = viewModelScope.launch {
      try {
          // Garante sala e marca como lido
          launch {
            try {
              repository.getOrCreateChatRoom(
                operatorId, operator.name ?: "Operador",
                userId, user.name ?: "Cliente"
              )
              repository.markAsRead(chatRoomId, currentSenderId)
              // Zera o contador local de não lidas para esta sala
              _unreadCount.value = _unreadCount.value + (chatRoomId to 0)
            } catch (e: Exception) {
              Log.e("ChatViewModel", "Erro ao garantir quarto/lido: ${e.message}")
            }
          }
          
          // Coleta mensagens em tempo real
          repository.getMessages(chatRoomId).collect { messageList ->
            if (currentChatRoomId == chatRoomId) {
                if (messageList.isNotEmpty() && messageList.last().senderId != currentSenderId && _messages.value.size < messageList.size) {
                    Toast.makeText(getApplication(), "WTC: Nova mensagem recebida!", Toast.LENGTH_SHORT).show()
                    // Marcar como lido automaticamente se a tela está aberta
                    repository.markAsRead(chatRoomId, currentSenderId)
                }
                _messages.value = messageList
            }
          }
      } catch (e: Exception) {
          Log.e("ChatViewModel", "ERRO no loadMessages para $chatRoomId", e)
      }
    }
  }

  private fun currentUserRoleIsOperator(): Boolean {
      return currentChatRoomId?.contains("_") == true
  }

  /**
   * Carrega mensagens de um canal de segmento.
   */
  fun loadGroupMessages(segment: String, currentSenderId: String) {
    val trimmedSegment = segment.trim()
    
    if (currentChatRoomId == trimmedSegment && loadMessagesJob?.isActive == true) {
        viewModelScope.launch { repository.markAsRead(trimmedSegment, currentSenderId) }
        return
    }
    
    Log.d("ChatViewModel", "loadGroupMessages INICIANDO: $trimmedSegment")
    
    loadMessagesJob?.cancel()
    _messages.value = emptyList()
    currentChatRoomId = trimmedSegment
    
    loadMessagesJob = viewModelScope.launch {
      try {
          launch {
              try { 
                  repository.markAsRead(trimmedSegment, currentSenderId) 
                  _unreadCount.value = _unreadCount.value + (trimmedSegment to 0)
              } catch (e: Exception) {}
          }
          repository.getGroupMessages(trimmedSegment).collect { messageList ->
            if (currentChatRoomId == trimmedSegment) {
                if (messageList.isNotEmpty() && messageList.last().senderId != currentSenderId && _messages.value.size < messageList.size) {
                    Toast.makeText(getApplication(), "WTC: Novidade no segmento!", Toast.LENGTH_SHORT).show()
                    repository.markAsRead(trimmedSegment, currentSenderId)
                }
                _messages.value = messageList
            }
          }
      } catch (e: Exception) {
          Log.e("ChatViewModel", "Erro no fluxo de grupo para $trimmedSegment", e)
      }
    }
  }

  fun sendGroupMessage(text: String, senderId: String, segment: String) {
    if (text.isBlank()) return
    val message = Message(senderId = senderId, text = text, timestamp = Date(), chatRoomId = segment)
    
    val currentList = _messages.value
    _messages.value = currentList + message

    viewModelScope.launch { 
        try {
            repository.sendGroupMessage(segment, message) 
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Erro ao enviar msg de grupo", e)
            _messages.value = currentList
        }
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
      operatorId = currentOperator.id?.trim() ?: "",
      operatorName = currentOperator.name ?: "",
      userId = currentUser.id?.trim() ?: "",
      userName = currentUser.name ?: ""
    )

    val message = Message(
      senderId = senderId,
      text = text,
      timestamp = Date(),
      chatRoomId = chatRoomId
    )

    val currentList = _messages.value
    _messages.value = currentList + message

    viewModelScope.launch {
      try {
        repository.sendMessage(chatRoomId, message, roomInfo)
      } catch (e: Exception) {
        Log.e("ChatViewModel", "Erro ao enviar msg 1:1", e)
        _messages.value = currentList
      }
    }
  }

  fun updateMessageStatus(messageId: String, status: String) {
    viewModelScope.launch {
      repository.updateMessageStatus(messageId, status)
    }
  }

  override fun onCleared() {
      super.onCleared()
      clearChatState()
  }
}
