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

    private val _vipFilter = MutableStateFlow("Todos")
    val vipFilter: StateFlow<String> = _vipFilter.asStateFlow()

    private val _stateFilter = MutableStateFlow("Todos")
    val stateFilter: StateFlow<String> = _stateFilter.asStateFlow()

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

    fun updateVipFilter(filter: String) {
        _vipFilter.value = filter
        filterCustomers()
    }

    fun updateStateFilter(filter: String) {
        _stateFilter.value = filter
        filterCustomers()
    }

    private fun filterCustomers() {
        val query = _searchQuery.value.lowercase()
        val vipFilter = _vipFilter.value
        val stateFilter = _stateFilter.value

        val filtered = _allCustomers.value.filter { customer ->
            val nameMatches = customer.name.lowercase().contains(query)
            val vipMatches = when (vipFilter) {
                "VIP" -> customer.vip
                "Não VIP" -> !customer.vip
                else -> true
            }
            val stateMatches = when (stateFilter) {
                "Todos" -> true
                else -> customer.estado.equals(stateFilter, ignoreCase = true)
            }
            nameMatches && vipMatches && stateMatches
        }

        _filteredCustomers.value = filtered
    }

}
