package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.Banner
import retrofit2.Response
import retrofit2.http.*

interface BannerApi {
    @GET("api/v1/banners")
    suspend fun getBanners(@Query("segment") segment: String? = null): Response<List<Banner>>

    @POST("api/v1/banners")
    suspend fun createBanner(@Body banner: Banner): Response<Banner>
}
