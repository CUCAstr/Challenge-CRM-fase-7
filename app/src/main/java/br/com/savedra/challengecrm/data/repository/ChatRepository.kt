package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.model.Message
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository {
  private val firestore: FirebaseFirestore = Firebase.firestore
  private val chatCollection = firestore.collection("chats")

  fun getChatRoomId(operatorId: String, userId: String): String {
    return if (operatorId > userId) {
      "${operatorId}_$userId"
    } else {
      "${userId}_$operatorId"
    }
  }

  fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
    val messageCollection = chatCollection.document(chatRoomId)
      .collection("messages")
      .orderBy("timestamp", Query.Direction.ASCENDING)

    val registration = messageCollection.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val messages = snapshot.toObjects(Message::class.java).mapIndexed { index, message ->
          message.copy(snapshot.documents[index].id)
        }

        trySend(messages)
      }
    }

    awaitClose { registration.remove() }
  }

  suspend fun sendMessage(chatRoomId: String, message: Message, chatRoomInfo: ChatRoom) {
    try {
      val participants = listOf(chatRoomInfo.operatorId, chatRoomInfo.userId)
      val roomData = mapOf(
        "operatorId" to chatRoomInfo.operatorId,
        "operatorName" to chatRoomInfo.operatorName,
        "userId" to chatRoomInfo.userId,
        "userName" to chatRoomInfo.userName,
        "participants" to participants,
        "lastMessageText" to message.text,
        "lastMessageTimestamp" to message.timestamp
      )

      chatCollection.document(chatRoomId).set(roomData, SetOptions.merge())

      chatCollection.document(chatRoomId)
        .collection("messages")
        .add(message)
        .await()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun getChatRooms(participantId: String): Flow<List<ChatRoom>> = callbackFlow {
    val query = chatCollection
      .whereArrayContains("participants", participantId)
      .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)

    val registration = query.addSnapshotListener { snapshot, e ->
      if (e != null) {
        close(e)
        return@addSnapshotListener
      }
      if (snapshot != null) {
        val rooms = snapshot.toObjects(ChatRoom::class.java).mapIndexed { index, room ->
          room.copy(id = snapshot.documents[index].id)
        }
        trySend(rooms)
      }
    }

    awaitClose { registration.remove() }
  }
}