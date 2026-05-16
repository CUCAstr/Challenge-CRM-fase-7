package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.PromotionApi
import br.com.savedra.challengecrm.model.Promotion

class PromotionRepository(private val promotionApi: PromotionApi) {

  suspend fun sendPromotion(promotion: Promotion) {
    promotionApi.createPromotion(promotion)
  }

  suspend fun getPromotions(userSegment: String? = null): List<Promotion> {
    val response = promotionApi.getPromotions(userSegment)
    return if (response.isSuccessful) {
        response.body() ?: emptyList()
    } else {
        emptyList()
    }
  }
}
