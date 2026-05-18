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
  fun loadMessages(operator: User, user: User) {
    val operatorId = operator.id?.trim() ?: "unknown_op"
    val userId = user.id?.trim() ?: "unknown_user"
    val chatRoomId = repository.getChatRoomId(operatorId, userId)
    
    if (currentChatRoomId == chatRoomId && loadMessagesJob?.isActive == true) {
        return
    }

    Log.d("ChatViewModel", "loadMessages INICIANDO: $chatRoomId")
    
    // CORREÇÃO: Limpar SEMPRE antes de iniciar nova coleta para evitar vazamento de dados (Bug 4)
    loadMessagesJob?.cancel()
    _messages.value = emptyList()
    currentChatRoomId = chatRoomId
    
    loadMessagesJob = viewModelScope.launch {
      try {
          // Garante sala no backend
          launch {
            try {
              repository.getOrCreateChatRoom(
                operatorId, operator.name ?: "Operador",
                userId, user.name ?: "Cliente"
              )
            } catch (e: Exception) {
              Log.e("ChatViewModel", "Erro ao garantir quarto: ${e.message}")
            }
          }
          
          // Coleta mensagens em tempo real
          repository.getMessages(chatRoomId).collect { messageList ->
            if (currentChatRoomId == chatRoomId) {
                _messages.value = messageList
            }
          }
      } catch (e: Exception) {
          Log.e("ChatViewModel", "ERRO no loadMessages para $chatRoomId", e)
      }
    }
  }

  /**
   * Carrega mensagens de um canal de segmento.
   */
  fun loadGroupMessages(segment: String) {
    val trimmedSegment = segment.trim()
    
    // Se já estamos monitorando este segmento, não reiniciamos o job
    if (currentChatRoomId == trimmedSegment && loadMessagesJob?.isActive == true) {
        Log.d("ChatViewModel", "loadGroupMessages: Já monitorando segmento $trimmedSegment")
        return
    }
    
    Log.d("ChatViewModel", "loadGroupMessages INICIANDO: $trimmedSegment")
    
    loadMessagesJob?.cancel()
    _messages.value = emptyList()
    currentChatRoomId = trimmedSegment
    
    loadMessagesJob = viewModelScope.launch {
      try {
          // Monitoramento único e limpo
          repository.getGroupMessages(trimmedSegment).collect { messageList ->
            // Filtro de segurança: só aceita se o ID da sala ainda for o mesmo no momento da emissão
            if (currentChatRoomId == trimmedSegment) {
                Log.d("ChatViewModel", "SEGMENTO $trimmedSegment: ${messageList.size} mensagens recebidas")
                _messages.value = messageList
            } else {
                Log.w("ChatViewModel", "Ignorando emissão de segmento antigo ($trimmedSegment != $currentChatRoomId)")
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
