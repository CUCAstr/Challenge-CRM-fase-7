package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {

    private val authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allCustomers = MutableStateFlow<List<User>>(emptyList())

    private val _filteredCustomers = MutableStateFlow<List<User>>(emptyList())
    val filteredCustomers: StateFlow<List<User>> = _filteredCustomers.asStateFlow()

    init {
        loadCustomers()
    }

    private fun loadCustomers() {
        viewModelScope.launch {
            val users = authRepository.getUsers()
            val customers = users.filter { it.role == "Cliente" }
            _allCustomers.value = customers
            filterCustomers()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterCustomers()
    }

    private fun filterCustomers() {
        val query = _searchQuery.value.lowercase()
        val filtered = if (query.isEmpty()) {
            _allCustomers.value
        } else {
            _allCustomers.value.filter { customer ->
                customer.name.lowercase().contains(query) ||
                customer.email.lowercase().contains(query)
            }
        }
        _filteredCustomers.value = filtered
    }

}
