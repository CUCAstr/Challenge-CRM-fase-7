package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Message
import com.google.firebase.firestore.FirebaseFirestore

class MessageRepository {

  private val db = FirebaseFirestore.getInstance()

  fun sendMessage(message: Message, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("messages")
      .add(message)
      .addOnSuccessListener {
        onSuccess()
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }
}