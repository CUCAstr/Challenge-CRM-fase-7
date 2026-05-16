package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.ChatApi
import br.com.savedra.challengecrm.data.api.WebSocketManager
import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class ChatRepository(private val chatApi: ChatApi) {
    private val webSocketManager = WebSocketManager("ws://10.0.2.2:8080/ws")
    private val gson = Gson()

    fun getChatRoomId(operatorId: String, userId: String): String {
        return if (operatorId > userId) {
            "${operatorId}_$userId"
        } else {
            "${userId}_$operatorId"
        }
    }

    fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        // Initial load
        val response = chatApi.getChatMessages(chatRoomId)
        if (response.isSuccessful) {
            trySend(response.body() ?: emptyList())
        }

        // Real-time updates via WebSocket
        webSocketManager.connect { json ->
            try {
                val message = gson.fromJson(json, Message::class.java)
                if (message.chatRoomId == chatRoomId) {
                    // Fetch list again for simplicity in this version
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        awaitClose { webSocketManager.disconnect() }
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

    // Missing methods for UI compatibility
    fun getGroupMessages(segment: String): Flow<List<Message>> = getMessages(segment)
    suspend fun sendGroupMessage(segment: String, message: Message) {
        chatApi.sendMessage(segment, message)
    }
}
