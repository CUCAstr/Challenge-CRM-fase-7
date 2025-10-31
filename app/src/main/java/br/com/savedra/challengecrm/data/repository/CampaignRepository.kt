package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Campaign
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CampaignRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendCampaign(campaign: Campaign) {
    firestore.collection("campaigns").add(campaign).await()
  }

  suspend fun getCampaigns(userSegment: String? = null): List<Campaign> {
    val query = if (userSegment != null) {
        firestore.collection("campaigns").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("campaigns")
    }
    val result = query.get().await()
    return result.map { document ->
        document.toObject(Campaign::class.java)
    }
  }
}