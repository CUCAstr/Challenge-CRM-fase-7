package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.InviteApi
import br.com.savedra.challengecrm.model.Invite

class InviteRepository(private val inviteApi: InviteApi) {

  suspend fun sendInvite(invite: Invite) {
    inviteApi.createInvite(invite)
  }

  suspend fun getInvites(userSegment: String? = null): List<Invite> {
    val response = inviteApi.getInvites(userSegment)
    return if (response.isSuccessful) {
        response.body() ?: emptyList()
    } else {
        emptyList()
    }
  }
}
