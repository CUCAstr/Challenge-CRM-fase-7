package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Campaign
import com.google.firebase.firestore.FirebaseFirestore

class CampaignRepository {
  private val firestore = FirebaseFirestore.getInstance()

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