package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Promotion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PromotionRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendPromotion(promotion: Promotion) {
    firestore.collection("promotions").add(promotion).await()
  }

  suspend fun getPromotions(userSegment: String? = null): List<Promotion> {
    val query = if (userSegment != null) {
        firestore.collection("promotions").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("promotions")
    }
    val result = query.get().await()
    return result.map { document ->
        document.toObject(Promotion::class.java)
    }
  }
}
