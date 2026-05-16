package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.BannerApi
import br.com.savedra.challengecrm.model.Banner

class BannerRepository(private val bannerApi: BannerApi) {

  suspend fun sendBanner(banner: Banner) {
    bannerApi.createBanner(banner)
  }

  suspend fun getBanners(userSegment: String? = null): List<Banner> {
    val response = bannerApi.getBanners(userSegment)
    return if (response.isSuccessful) {
        response.body() ?: emptyList()
    } else {
        emptyList()
    }
  }
}
