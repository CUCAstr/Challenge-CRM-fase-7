package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.data.repository.InviteRepository
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InviteViewModel : ViewModel() {

  private val inviteRepository = InviteRepository()
  private val authRepository =
    AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _allInvites = MutableStateFlow<List<Invite>>(emptyList())

  private val _filteredInvites = MutableStateFlow<List<Invite>>(emptyList())
  val filteredInvites: StateFlow<List<Invite>> = _filteredInvites.asStateFlow()

  private val _newInviteTitle = MutableStateFlow("")
  val newInviteTitle: StateFlow<String> = _newInviteTitle.asStateFlow()

  private val _newInviteDescription = MutableStateFlow("")
  val newInviteDescription: StateFlow<String> = _newInviteDescription.asStateFlow()

  private val _newInviteDate = MutableStateFlow("")
  val newInviteDate: StateFlow<String> = _newInviteDate.asStateFlow()

  private val _newInviteLocation = MutableStateFlow("")
  val newInviteLocation: StateFlow<String> = _newInviteLocation.asStateFlow()

  private val _newInviteTime = MutableStateFlow("")
  val newInviteTime: StateFlow<String> = _newInviteTime.asStateFlow()

  private val _segmentFilter = MutableStateFlow("Todos")
  val segmentFilter: StateFlow<String> = _segmentFilter.asStateFlow()

  private val _estadoFilter = MutableStateFlow("Todos")
  val estadoFilter: StateFlow<String> = _estadoFilter.asStateFlow()

  private val _statusFilter = MutableStateFlow("Todos")
  val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

  private val _scoreStartFilter = MutableStateFlow("")
  val scoreStartFilter: StateFlow<String> = _scoreStartFilter.asStateFlow()

  private val _scoreEndFilter = MutableStateFlow("")
  val scoreEndFilter: StateFlow<String> = _scoreEndFilter.asStateFlow()

  private val _filteredClients = MutableStateFlow<List<User>>(emptyList())
  val filteredClients: StateFlow<List<User>> = _filteredClients.asStateFlow()

  init {
    loadInvites()
  }

  private fun loadInvites() {
    viewModelScope.launch {
      inviteRepository.getInvites(
        onSuccess = {
          _allInvites.value = it
          filterInvites()
        },
        onFailure = {
          // Handle error
        }
      )
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    filterInvites()
  }

  private fun filterInvites() {
    val query = _searchQuery.value.lowercase()

    val filtered = _allInvites.value.filter { invite ->
      val nameMatches = invite.name.lowercase().contains(query)
      nameMatches
    }

    _filteredInvites.value = filtered
  }

  fun onNewInviteTitleChange(title: String) {
    _newInviteTitle.value = title
  }

  fun onNewInviteDescriptionChange(description: String) {
    _newInviteDescription.value = description
  }

  fun onNewInviteDateChange(date: String) {
    _newInviteDate.value = date
  }

  fun onNewInviteLocationChange(location: String) {
    _newInviteLocation.value = location
  }

  fun onNewInviteTimeChange(time: String) {
    _newInviteTime.value = time
  }

  fun onSegmentFilterChange(segment: String) {
    _segmentFilter.value = segment
  }

  fun onEstadoFilterChange(estado: String) {
    _estadoFilter.value = estado
  }

  fun onStatusFilterChange(status: String) {
    _statusFilter.value = status
  }

  fun onScoreStartFilterChange(score: String) {
    _scoreStartFilter.value = score
  }

  fun onScoreEndFilterChange(score: String) {
    _scoreEndFilter.value = score
  }

  fun sendInvite() {
    val invite = Invite(
      name = _newInviteTitle.value,
      description = _newInviteDescription.value,
      date = _newInviteDate.value,
      time = _newInviteTime.value,
      location = _newInviteLocation.value
    )
    inviteRepository.sendInvite(invite, onSuccess = { loadInvites() }, onFailure = {})
  }

  fun getFilteredClients() {
    viewModelScope.launch {
      val allUsers = authRepository.getUsers()
      val allCostumers = allUsers.filter { it.role == "Cliente" }
      val segmentFilter = _segmentFilter.value
      val stateFilter = _estadoFilter.value
      val statusFilter = _statusFilter.value

      val filtered = allCostumers.filter { user ->
        val segmentMatches = when (segmentFilter) {
          "Todos" -> true
          else -> user.segment.equals(segmentFilter, ignoreCase = true)
        }
        val stateMatches = when (stateFilter) {
          "Todos" -> true
          else -> user.estado.equals(stateFilter, ignoreCase = true)
        }
        val statusMatches = when (statusFilter) {
          "Todos" -> true
          else -> user.status.equals(statusFilter, ignoreCase = true)
        }

        val scoreStart = _scoreStartFilter.value.toIntOrNull() ?: 0
        val scoreEnd = _scoreEndFilter.value.toIntOrNull() ?: Int.MAX_VALUE
        val scoreMatches = user.score in scoreStart..scoreEnd

        segmentMatches && stateMatches && statusMatches && scoreMatches
      }

      _filteredClients.value = filtered
    }
  }
}