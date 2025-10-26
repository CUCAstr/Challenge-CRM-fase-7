package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Campaign
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CampaignRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun addCampaign(campaign: Campaign) {
        firestore.collection("campaigns").add(campaign).await()
    }
}
