package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Promotion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PromotionRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendPromotion(promotion: Promotion) {
    firestore.collection("promotions").add(promotion).await()
  }

  fun getPromotions(userSegment: String? = null, onSuccess: (List<Promotion>) -> Unit, onFailure: (Exception) -> Unit) {
    val query = if (userSegment != null) {
        firestore.collection("promotions").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("promotions")
    }
    query.get()
      .addOnSuccessListener { result ->
        val promotions = result.map { document ->
          document.toObject(Promotion::class.java)
        }
        onSuccess(promotions)
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }
}
