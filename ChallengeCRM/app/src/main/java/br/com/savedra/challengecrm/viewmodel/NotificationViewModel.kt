package br.com.savedra.challengecrm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.*
import br.com.savedra.challengecrm.di.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val bannerRepository = RepositoryProvider.getBannerRepository(application)
    private val campaignRepository = RepositoryProvider.getCampaignRepository(application)
    private val inviteRepository = RepositoryProvider.getInviteRepository(application)
    private val promotionRepository = RepositoryProvider.getPromotionRepository(application)

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
