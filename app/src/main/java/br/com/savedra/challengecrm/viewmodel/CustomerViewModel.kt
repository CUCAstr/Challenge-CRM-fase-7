package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CustomerViewModel : ViewModel() {
    private val _customers = MutableStateFlow<List<User>>(emptyList())
    val customers: StateFlow<List<User>> = _customers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredCustomers = MutableStateFlow<List<User>>(emptyList())
    val filteredCustomers: StateFlow<List<User>> = _filteredCustomers.asStateFlow()

    init {
        loadMockCustomers()
    }

    private fun loadMockCustomers() {
        val mockCustomers = listOf(
            User(
                id = "1",
                name = "Ana Costa",
                email = "ana.costa@example.com",
                password = "",
                role = "cliente",
                memberSince = Date(1689462000000) // 15/07/2023
            ),
            User(
                id = "2",
                name = "Bruno Dias",
                email = "bruno.dias@example.com",
                password = "",
                role = "cliente",
                memberSince = Date(1689462000000)
            ),
            User(
                id = "3",
                name = "Carla Faria",
                email = "carla.faria@example.com",
                password = "",
                role = "cliente",
                memberSince = Date(1689462000000)
            ),
            User(
                id = "4",
                name = "Diego Silva",
                email = "diego.silva@example.com",
                password = "",
                role = "cliente",
                memberSince = Date(1689462000000)
            )
        )
        _customers.value = mockCustomers
        _filteredCustomers.value = mockCustomers
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterCustomers()
    }

    private fun filterCustomers() {
        val query = _searchQuery.value.lowercase()
        val filtered = if (query.isEmpty()) {
            _customers.value
        } else {
            _customers.value.filter { customer ->
                customer.name.lowercase().contains(query) ||
                customer.email.lowercase().contains(query)
            }
        }
        _filteredCustomers.value = filtered
    }

    fun addNoteToCustomer(customerId: String, note: String) {
        viewModelScope.launch {
            val updatedCustomers = _customers.value.map { customer ->
                if (customer.id == customerId) {
                    customer.copy(notes = customer.notes + note)
                } else {
                    customer
                }
            }
            _customers.value = updatedCustomers
            filterCustomers()
        }
    }
}
