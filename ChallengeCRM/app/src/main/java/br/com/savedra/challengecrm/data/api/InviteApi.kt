package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.Invite
import retrofit2.Response
import retrofit2.http.*

interface InviteApi {
    @GET("api/v1/invites")
    suspend fun getInvites(@Query("segment") segment: String? = null): Response<List<Invite>>

    @POST("api/v1/invites")
    suspend fun createInvite(@Body invite: Invite): Response<Invite>
}
