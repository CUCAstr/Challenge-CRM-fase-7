package br.com.savedra.challengecrm

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.savedra.challengecrm.data.repository.*
import br.com.savedra.challengecrm.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PopulateDatabaseTest {

    private val bannerRepository = BannerRepository()
    private val campaignRepository = CampaignRepository()
    private val inviteRepository = InviteRepository()
    private val promotionRepository = PromotionRepository()

    @Test
    fun populateDatabase() {
        runBlocking {
            // Banners
            bannerRepository.sendBanner(Banner(title = "Banner IT", description = "Banner for IT segment", imageUrl = "", segment = "IT"), {}, {})
            bannerRepository.sendBanner(Banner(title = "Banner Todos", description = "Banner for all segments", imageUrl = "", segment = "Todos"), {}, {})
            bannerRepository.sendBanner(Banner(title = "Banner Finance", description = "Banner for Finance segment", imageUrl = "", segment = "Finance"), {}, {})

            // Campaigns
            campaignRepository.sendCampaign(Campaign(title = "Campaign IT", description = "Campaign for IT segment", startDate = "01/01/2025", endDate = "31/12/2025", segment = "IT"), {}, {})
            campaignRepository.sendCampaign(Campaign(title = "Campaign Todos", description = "Campaign for all segments", startDate = "01/01/2025", endDate = "31/12/2025", segment = "Todos"), {}, {})
            campaignRepository.sendCampaign(Campaign(title = "Campaign HR", description = "Campaign for HR segment", startDate = "01/01/2025", endDate = "31/12/2025", segment = "HR"), {}, {})

            // Invites
            inviteRepository.sendInvite(Invite(name = "Invite IT", description = "Invite for IT segment", date = "01/01/2025", time = "10:00", location = "Online", segment = "IT"), {}, {})
            inviteRepository.sendInvite(Invite(name = "Invite Todos", description = "Invite for all segments", date = "01/01/2025", time = "10:00", location = "Online", segment = "Todos"), {}, {})
            inviteRepository.sendInvite(Invite(name = "Invite Retail", description = "Invite for Retail segment", date = "01/01/2025", time = "10:00", location = "Online", segment = "Retail & Financial"), {}, {})

            // Promotions
            promotionRepository.sendPromotion(Promotion(title = "Promotion IT", description = "Promotion for IT segment", originalValue = "100", promotionValue = "80", dateExpiresIn = "31/12/2025", hoursExpiresIn = "23:59", segment = "IT"), {}, {})
            promotionRepository.sendPromotion(Promotion(title = "Promotion Todos", description = "Promotion for all segments", originalValue = "100", promotionValue = "80", dateExpiresIn = "31/12/2025", hoursExpiresIn = "23:59", segment = "Todos"), {}, {})
            promotionRepository.sendPromotion(Promotion(title = "Promotion GRC", description = "Promotion for GRC segment", originalValue = "100", promotionValue = "80", dateExpiresIn = "31/12/2025", hoursExpiresIn = "23:59", segment = "GRC"), {}, {})
        }
    }
}