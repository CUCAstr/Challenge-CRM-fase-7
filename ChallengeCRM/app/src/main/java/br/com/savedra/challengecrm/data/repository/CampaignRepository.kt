package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.CampaignApi
import br.com.savedra.challengecrm.model.Campaign

class CampaignRepository(private val campaignApi: CampaignApi) {

  suspend fun sendCampaign(campaign: Campaign) {
    campaignApi.createCampaign(campaign)
  }

  suspend fun getCampaigns(userSegment: String? = null): List<Campaign> {
    val response = campaignApi.getCampaigns(userSegment)
    return if (response.isSuccessful) {
        response.body() ?: emptyList()
    } else {
        emptyList()
    }
  }
}
