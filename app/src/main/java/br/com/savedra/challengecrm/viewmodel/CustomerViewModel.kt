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

    private val _segmentFilter = MutableStateFlow("Todos")
    val segmentFilter: StateFlow<String> = _segmentFilter.asStateFlow()

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

    fun updateSegmentFilter(filter: String) {
        _segmentFilter.value = filter
        filterCustomers()
    }

    fun updateStateFilter(filter: String) {
        _stateFilter.value = filter
        filterCustomers()
    }

    private fun filterCustomers() {
        val query = _searchQuery.value.lowercase()
        val segmentFilter = _segmentFilter.value
        val stateFilter = _stateFilter.value

        val filtered = _allCustomers.value.filter { customer ->
            val nameMatches = customer.name.lowercase().contains(query)
            val statusMatches = customer.status.lowercase().contains(query)

            val segmentMatches = when (segmentFilter) {
                "Todos" -> true
                else -> customer.segment.equals(segmentFilter, ignoreCase = true)
            }

            val stateMatches = when (stateFilter) {
                "Todos" -> true
                else -> customer.estado.equals(stateFilter, ignoreCase = true)
            }

            nameMatches && segmentMatches && stateMatches && statusMatches
        }

        _filteredCustomers.value = filtered
    }

    fun saveNotes(userId: String, notes: String) {
        viewModelScope.launch {
            authRepository.updateUserNotes(userId, notes)
            loadCustomers()
        }
    }

}
