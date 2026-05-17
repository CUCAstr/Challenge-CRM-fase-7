package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class AuthenticationRequest(val email: String, val password: String)
data class AuthenticationResponse(val token: String)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val company: String,
    val segment: String,
    val gender: String,
    val phone: String,
    val category: String
)

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthenticationResponse>

    @POST("api/v1/auth/authenticate")
    suspend fun authenticate(@Body request: AuthenticationRequest): Response<AuthenticationResponse>

    @GET("api/v1/auth/me")
    suspend fun getMe(): Response<User>
}
