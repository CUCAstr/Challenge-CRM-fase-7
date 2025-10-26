package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Campaign
import br.com.savedra.challengecrm.model.Invite
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CampaignRepository(
    private val firestore: FirebaseFirestore
) {
    fun sendCampaign(campaign: Campaign, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("campaigns")
            .add(campaign)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
