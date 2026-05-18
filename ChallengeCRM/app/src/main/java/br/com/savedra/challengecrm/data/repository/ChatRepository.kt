package br.com.savedra.challengecrm.data.repository

import android.util.Log
import br.com.savedra.challengecrm.data.api.ChatApi
import br.com.savedra.challengecrm.data.api.WebSocketManager
import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Repositório de Chat com integração REST e WebSocket.
 */
class ChatRepository(private val chatApi: ChatApi) {
    // CORREÇÃO: Usar lazy para evitar que a conexão abra no momento que a ViewModel é criada (causando crash se o backend demorar)
    private val webSocketManager by lazy { WebSocketManager("ws://10.0.2.2:8080/ws") }
    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)

    fun getChatRoomId(operatorId: String, userId: String): String {
        return if (operatorId > userId) {
            "${operatorId}_$userId"
        } else {
            "${userId}_$operatorId"
        }
    }

    fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        /**
         * Busca a lista completa de mensagens do servidor.
         */
        suspend fun fetchAndSend() {
            try {
                val response = chatApi.getChatMessages(chatRoomId)
                if (response.isSuccessful) {
                    trySend(response.body() ?: emptyList())
                }
            } catch (e: Exception) {
                Log.e("ChatRepo", "Erro ao buscar histórico", e)
            }
        }

        // Carga inicial
        scope.launch { fetchAndSend() }

        // --- CONEXÃO WEBSOCKET ---
        webSocketManager.connect { json ->
            try {
                val message = gson.fromJson(json, Message::class.java)
                if (message.chatRoomId == chatRoomId) {
                    Log.d("ChatRepo", "Mensagem recebida via WS. Atualizando lista...")
                    scope.launch { fetchAndSend() }
                }
            } catch (e: Exception) {
                Log.e("ChatRepo", "Erro no processamento de mensagem WS", e)
            }
        }

        awaitClose { 
            Log.d("ChatRepo", "Desconectando WebSocket")
            webSocketManager.disconnect() 
        }
    }

    suspend fun sendMessage(chatRoomId: String, message: Message, chatRoomInfo: ChatRoom) {
        chatApi.sendMessage(chatRoomId, message)
    }

    fun getChatRooms(participantId: String): Flow<List<ChatRoom>> = flow {
        val response = chatApi.getUserChatRooms(participantId)
        if (response.isSuccessful) {
            emit(response.body() ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    suspend fun updateMessageStatus(messageId: String, status: String) {
        chatApi.updateMessageStatus(messageId, status)
    }

    suspend fun getOrCreateChatRoom(operatorId: String, operatorName: String, userId: String, userName: String): ChatRoom? {
        val response = chatApi.getOrCreateChatRoom(operatorId, operatorName, userId, userName)
        return if (response.isSuccessful) response.body() else null
    }

    // Compatibilidade com UI Legada
    fun getGroupMessages(segment: String): Flow<List<Message>> = getMessages(segment)
    suspend fun sendGroupMessage(segment: String, message: Message) {
        chatApi.sendMessage(segment, message)
    }
}
