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
import kotlinx.coroutines.*

/**
 * Repositório de Chat com integração REST e WebSocket.
 * 
 * CORREÇÃO (B24): 
 * 1. Implementado Polling de backup (5s) para garantir que o histórico seja atualizado 
 *    mesmo que o WebSocket falhe ou a conexão oscile.
 * 2. Removida dependência exclusiva do scope global para evitar leaks.
 */
class ChatRepository(private val chatApi: ChatApi) {
    private val webSocketManager by lazy { WebSocketManager("ws://10.0.2.2:8080/ws") }
    private val gson = com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()

    fun getChatRoomId(operatorId: String, userId: String): String {
        val opId = operatorId.trim()
        val uId = userId.trim()
        // Ordem alfabética para garantir ID único independente de quem inicia
        return if (opId < uId) "${opId}_$uId" else "${uId}_$opId"
    }

    fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        Log.d("ChatRepo", "--- MONITORANDO: $chatRoomId ---")

        val fetchJob = launch {
            while (isActive) {
                try {
                    Log.d("ChatRepo", "[$chatRoomId] Iniciando Sync via REST...")
                    val response = chatApi.getChatMessages(chatRoomId)
                    if (response.isSuccessful) {
                        val messages = response.body() ?: emptyList()
                        Log.d("ChatRepo", "[$chatRoomId] DB Sync: ${messages.size} mensagens recebidas do servidor.")
                        if (messages.isEmpty()) {
                            Log.w("ChatRepo", "[$chatRoomId] AVISO: O servidor retornou uma lista VAZIA.")
                        }
                        trySend(messages)
                    } else {
                        Log.e("ChatRepo", "[$chatRoomId] ERRO REST: Código=${response.code()} - ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("ChatRepo", "[$chatRoomId] EXCEÇÃO CRÍTICA no Polling: ${e.message}", e)
                }
                delay(5000)
            }
        }

        webSocketManager.connect { json ->
            try {
                Log.d("ChatRepo", "[$chatRoomId] Evento WS bruto recebido: $json")
                val message = gson.fromJson(json, Message::class.java)
                if (message.chatRoomId == chatRoomId) {
                    Log.d("ChatRepo", "[$chatRoomId] Evento WS confirmado para esta sala.")
                }
            } catch (e: Exception) { 
                Log.e("ChatRepo", "[$chatRoomId] Falha ao processar JSON do WS: ${e.message}")
            }
        }

        awaitClose { 
            Log.d("ChatRepo", "--- ENCERRANDO MONITORAMENTO: $chatRoomId ---")
            fetchJob.cancel()
            webSocketManager.disconnect() 
        }
    }

    suspend fun sendMessage(chatRoomId: String, message: Message, chatRoomInfo: ChatRoom) {
        val response = chatApi.sendMessage(chatRoomId, message)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("ChatRepo", "ERRO AO ENVIAR ($chatRoomId): $errorBody")
            throw Exception("Envio falhou")
        }
        Log.d("ChatRepo", "Mensagem enviada com sucesso para $chatRoomId")
    }

    fun getChatRooms(participantId: String): Flow<List<ChatRoom>> = flow {
        while(true) {
            val response = chatApi.getUserChatRooms(participantId)
            if (response.isSuccessful) emit(response.body() ?: emptyList())
            delay(10000)
        }
    }

    suspend fun updateMessageStatus(messageId: String, status: String) {
        chatApi.updateMessageStatus(messageId, status)
    }

    suspend fun getOrCreateChatRoom(operatorId: String, operatorName: String, userId: String, userName: String): ChatRoom? {
        val response = chatApi.getOrCreateChatRoom(operatorId, operatorName, userId, userName)
        return if (response.isSuccessful) response.body() else null
    }

    fun getGroupMessages(segment: String): Flow<List<Message>> = getMessages(segment)
    
    suspend fun sendGroupMessage(segment: String, message: Message) {
        sendMessage(segment, message, ChatRoom())
    }
}
