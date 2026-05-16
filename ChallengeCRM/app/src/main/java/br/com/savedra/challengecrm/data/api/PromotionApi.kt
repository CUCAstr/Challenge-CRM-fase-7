package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.Promotion
import retrofit2.Response
import retrofit2.http.*

interface PromotionApi {
    @GET("api/v1/promotions")
    suspend fun getPromotions(@Query("segment") segment: String? = null): Response<List<Promotion>>

    @POST("api/v1/promotions")
    suspend fun createPromotion(@Body promotion: Promotion): Response<Promotion>
}
