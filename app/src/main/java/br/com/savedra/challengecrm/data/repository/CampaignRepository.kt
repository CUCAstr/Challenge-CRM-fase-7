package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Campaign
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CampaignRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendCampaign(campaign: Campaign) {
    firestore.collection("campaigns").add(campaign).await()
  }

  fun getCampaigns(userSegment: String? = null, onSuccess: (List<Campaign>) -> Unit, onFailure: (Exception) -> Unit) {
    val query = if (userSegment != null) {
        firestore.collection("campaigns").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("campaigns")
    }
    query.get()
      .addOnSuccessListener { result ->
        val campaigns = result.map { document ->
          document.toObject(Campaign::class.java)
        }
        onSuccess(campaigns)
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }
}