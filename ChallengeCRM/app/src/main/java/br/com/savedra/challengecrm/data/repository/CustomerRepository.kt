package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.data.api.CustomerApi
import br.com.savedra.challengecrm.model.User

class CustomerRepository(private val customerApi: CustomerApi) {
    suspend fun getCustomers(segment: String? = null): List<User> {
        val response = customerApi.getAllCustomers(segment)
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getCustomerById(id: String): User? {
        val response = customerApi.getCustomerById(id)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createCustomer(user: User): User? {
        val response = customerApi.createCustomer(user)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateCustomer(id: String, user: User): User? {
        val response = customerApi.updateCustomer(id, user)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteCustomer(id: String): Boolean {
        return customerApi.deleteCustomer(id).isSuccessful
    }

    suspend fun getCustomerTimeline(id: String): User? {
        val response = customerApi.getCustomerTimeline(id)
        return if (response.isSuccessful) response.body() else null
    }
}
