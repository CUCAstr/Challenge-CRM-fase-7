package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Invite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InviteRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendInvite(invite: Invite) {
    firestore.collection("invites").add(invite).await()
  }

  fun getInvites(userSegment: String? = null, onSuccess: (List<Invite>) -> Unit, onFailure: (Exception) -> Unit) {
    val query = if (userSegment != null) {
        firestore.collection("invites").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("invites")
    }
    query.get()
      .addOnSuccessListener { result ->
        val invites = result.map { document ->
          document.toObject(Invite::class.java)
        }
        onSuccess(invites)
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }
}