package br.com.savedra.challengecrm.di

import android.content.Context
import br.com.savedra.challengecrm.data.api.*
import br.com.savedra.challengecrm.data.local.TokenManager
import br.com.savedra.challengecrm.data.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object RepositoryProvider {
    private var tokenManager: TokenManager? = null

    fun getTokenManager(context: Context): TokenManager {
        if (tokenManager == null) {
            tokenManager = TokenManager(context.applicationContext)
        }
        return tokenManager!!
    }

    fun getAuthRepository(context: Context): AuthRepository {
        val tm = getTokenManager(context)
        return AuthRepository(
            ApiClient.getAuthApi(tm),
            ApiClient.getApi(CustomerApi::class.java, tm), // Injeta a API de Clientes para busca de usuários
            tm
        )
    }

    fun getCustomerRepository(context: Context): CustomerRepository {
        return CustomerRepository(
            ApiClient.getApi(CustomerApi::class.java, getTokenManager(context))
        )
    }

    fun getChatRepository(context: Context): ChatRepository {
        return ChatRepository(
            ApiClient.getApi(ChatApi::class.java, getTokenManager(context))
        )
    }

    fun getCampaignRepository(context: Context): CampaignRepository {
        return CampaignRepository(
            ApiClient.getApi(CampaignApi::class.java, getTokenManager(context))
        )
    }

    fun getInviteRepository(context: Context): InviteRepository {
        return InviteRepository(
            ApiClient.getApi(InviteApi::class.java, getTokenManager(context))
        )
    }

    fun getPromotionRepository(context: Context): PromotionRepository {
        return PromotionRepository(
            ApiClient.getApi(PromotionApi::class.java, getTokenManager(context))
        )
    }

    fun getBannerRepository(context: Context): BannerRepository {
        return BannerRepository(
            ApiClient.getApi(BannerApi::class.java, getTokenManager(context))
        )
    }
}
