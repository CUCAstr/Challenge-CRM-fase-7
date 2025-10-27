package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Invite
import com.google.firebase.firestore.FirebaseFirestore

class InviteRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun sendInvite(invite: Invite, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("invites")
            .add(invite)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getInvites(onSuccess: (List<Invite>) -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("invites")
            .get()
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