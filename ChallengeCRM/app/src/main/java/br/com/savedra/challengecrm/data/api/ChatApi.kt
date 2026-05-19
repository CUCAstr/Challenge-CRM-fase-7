package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {
    @GET("api/v1/chat/rooms/{userId}")
    suspend fun getUserChatRooms(@Path("userId") userId: String): Response<List<ChatRoom>>

    @GET("api/v1/chat/messages/{chatRoomId}")
    suspend fun getChatMessages(@Path("chatRoomId") chatRoomId: String): Response<List<Message>>

    @POST("api/v1/chat/messages/{chatRoomId}")
    suspend fun sendMessage(@Path("chatRoomId") chatRoomId: String, @Body message: Message): Response<Message>

    @POST("api/v1/chat/rooms")
    suspend fun getOrCreateChatRoom(
        @Query("operatorId") operatorId: String,
        @Query("operatorName") operatorName: String,
        @Query("userId") userId: String,
        @Query("userName") userName: String
    ): Response<ChatRoom>

    @PATCH("api/v1/chat/messages/{messageId}/status")
    suspend fun updateMessageStatus(
        @Path("messageId") messageId: String,
        @Query("status") status: String
    ): Response<Void>

    @PATCH("api/v1/chat/rooms/{chatRoomId}/read")
    suspend fun markAsRead(
        @Path("chatRoomId") chatRoomId: String,
        @Query("userId") userId: String
    ): Response<Void>
}
