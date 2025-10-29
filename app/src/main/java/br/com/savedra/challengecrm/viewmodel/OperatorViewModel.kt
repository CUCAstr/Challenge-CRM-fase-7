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

class OperatorViewModel : ViewModel() {
  private val authRepository =
    AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _segmentFilter = MutableStateFlow("Todos")
  val segmentFilter: StateFlow<String> = _segmentFilter.asStateFlow()

  private val _statusFilter = MutableStateFlow("Todos")
  val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

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

  fun updateStatusFilter(filter: String) {
    _statusFilter.value = filter
    filterCustomers()
  }

  private fun filterCustomers() {
    val query = _searchQuery.value.lowercase()
    val segmentFilter = _segmentFilter.value
    val statusFilter = _statusFilter.value

    val filtered = _allCustomers.value.filter { customer ->
      val nameMatches = customer.name.lowercase().contains(query)

      val segmentMatches = when (segmentFilter) {
        "Todos" -> true
        else -> customer.segment.equals(segmentFilter, ignoreCase = true)
      }

      val statusMatches = when (statusFilter) {
        "Todos" -> true
        else -> customer.status.equals(statusFilter, ignoreCase = true)
      }

      nameMatches && segmentMatches && statusMatches
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
