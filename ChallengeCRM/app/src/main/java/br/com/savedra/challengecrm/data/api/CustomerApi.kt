package br.com.savedra.challengecrm.data.api

import br.com.savedra.challengecrm.model.User
import retrofit2.Response
import retrofit2.http.*

interface CustomerApi {
    @GET("api/v1/customers")
    suspend fun getAllCustomers(@Query("segment") segment: String? = null): Response<List<User>>

    @GET("api/v1/customers/{id}")
    suspend fun getCustomerById(@Path("id") id: String): Response<User>

    @POST("api/v1/customers")
    suspend fun createCustomer(@Body user: User): Response<User>

    @PUT("api/v1/customers/{id}")
    suspend fun updateCustomer(@Path("id") id: String, @Body user: User): Response<User>

    @DELETE("api/v1/customers/{id}")
    suspend fun deleteCustomer(@Path("id") id: String): Response<Void>

    @GET("api/v1/customers/{id}/timeline")
    suspend fun getCustomerTimeline(@Path("id") id: String): Response<User>
}
