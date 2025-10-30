package br.com.savedra.challengecrm.viewmodel

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
        viewModelScope.launch {
            var totalCount = 0
            bannerRepository.getBanners(
                userSegment = userSegment,
                onSuccess = { banners ->
                    totalCount += banners.size
                    updateNotificationMessage(totalCount)
                },
                onFailure = { /* Handle failure */ }
            )
            campaignRepository.getCampaigns(
                userSegment = userSegment,
                onSuccess = { campaigns ->
                    totalCount += campaigns.size
                    updateNotificationMessage(totalCount)
                },
                onFailure = { /* Handle failure */ }
            )
            inviteRepository.getInvites(
                userSegment = userSegment,
                onSuccess = { invites ->
                    totalCount += invites.size
                    updateNotificationMessage(totalCount)
                },
                onFailure = { /* Handle failure */ }
            )
            promotionRepository.getPromotions(
                userSegment = userSegment,
                onSuccess = { promotions ->
                    totalCount += promotions.size
                    updateNotificationMessage(totalCount)
                },
                onFailure = { /* Handle failure */ }
            )
        }
    }

    private fun updateNotificationMessage(count: Int) {
        if (count > 0) {
            _notificationMessage.value = "Você tem $count novas notificações."
            _showNotification.value = true
        }
    }

    fun dismissNotification() {
        _showNotification.value = false
    }
}