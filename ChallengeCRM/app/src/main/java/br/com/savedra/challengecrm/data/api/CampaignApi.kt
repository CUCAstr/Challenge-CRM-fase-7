package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.Campaign
import retrofit2.Response
import retrofit2.http.*

interface CampaignApi {
    @GET("api/v1/campaigns")
    suspend fun getCampaigns(@Query("segment") segment: String? = null): Response<List<Campaign>>

    @GET("api/v1/campaigns/{id}")
    suspend fun getCampaignById(@Path("id") id: String): Response<Campaign>

    @POST("api/v1/campaigns")
    suspend fun createCampaign(@Body campaign: Campaign): Response<Campaign>

    @DELETE("api/v1/campaigns/{id}")
    suspend fun deleteCampaign(@Path("id") id: String): Response<Void>
}
