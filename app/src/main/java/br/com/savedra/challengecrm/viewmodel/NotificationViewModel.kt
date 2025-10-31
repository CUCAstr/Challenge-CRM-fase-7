package br.com.savedra.challengecrm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val bannerRepository: BannerRepository = BannerRepository(),
    private val campaignRepository: CampaignRepository = CampaignRepository(),
    private val inviteRepository: InviteRepository = InviteRepository(),
    private val promotionRepository: PromotionRepository = PromotionRepository()
) : ViewModel() {

    private val _notificationMessage = MutableStateFlow("")
    val notificationMessage: StateFlow<String> = _notificationMessage

    private val _showNotification = MutableStateFlow(false)
    val showNotification: StateFlow<Boolean> = _showNotification

    fun fetchNotificationCounts(userSegment: String) {
        Log.d("NotificationDebug", "[ViewModel] fetchNotificationCounts called with segment: $userSegment")
        viewModelScope.launch {
            try {
                val banners = bannerRepository.getBanners(userSegment)
                Log.d("NotificationDebug", "[ViewModel] Fetched Banners count: ${banners.size}")

                val campaigns = campaignRepository.getCampaigns(userSegment)
                Log.d("NotificationDebug", "[ViewModel] Fetched Campaigns count: ${campaigns.size}")

                val invites = inviteRepository.getInvites(userSegment)
                Log.d("NotificationDebug", "[ViewModel] Fetched Invites count: ${invites.size}")

                val promotions = promotionRepository.getPromotions(userSegment)
                Log.d("NotificationDebug", "[ViewModel] Fetched Promotions count: ${promotions.size}")

                val totalCount = banners.size + campaigns.size + invites.size + promotions.size
                Log.d("NotificationDebug", "[ViewModel] Total notification count: $totalCount")

                updateNotificationMessage(totalCount)
            } catch (e: Exception) {
                Log.e("NotificationDebug", "[ViewModel] Error fetching notification counts", e)
            }
        }
    }

    private fun updateNotificationMessage(count: Int) {
        Log.d("NotificationDebug", "[ViewModel] updateNotificationMessage called with count: $count")
        if (count > 0) {
            _notificationMessage.value = "Você tem $count novas notificações."
            _showNotification.value = true
            Log.d("NotificationDebug", "[ViewModel] showNotification set to true")
        } else {
            Log.d("NotificationDebug", "[ViewModel] Total count is 0, notification will not be shown.")
        }
    }

    fun dismissNotification() {
        _showNotification.value = false
    }
}