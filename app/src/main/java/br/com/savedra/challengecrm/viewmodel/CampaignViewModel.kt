package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.data.repository.CampaignRepository
import br.com.savedra.challengecrm.model.Campaign
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CampaignViewModel : ViewModel() {

  private val campaignRepository = CampaignRepository()
  private val authRepository =
    AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _allCampaigns = MutableStateFlow<List<Campaign>>(emptyList())

  private val _filteredCampaigns = MutableStateFlow<List<Campaign>>(emptyList())
  val filteredCampaigns: StateFlow<List<Campaign>> = _filteredCampaigns.asStateFlow()

  private val _newCampaignTitle = MutableStateFlow("")
  val newCampaignTitle: StateFlow<String> = _newCampaignTitle.asStateFlow()

  private val _newCampaignDescription = MutableStateFlow("")
  val newCampaignDescription: StateFlow<String> = _newCampaignDescription.asStateFlow()

  private val _newCampaignStartDate = MutableStateFlow("")
  val newCampaignStartDate: StateFlow<String> = _newCampaignStartDate.asStateFlow()

  private val _newCampaignEndDate = MutableStateFlow("")
  val newCampaignEndDate: StateFlow<String> = _newCampaignEndDate.asStateFlow()

  private val _segmentFilter = MutableStateFlow("Todos")
  val segmentFilter: StateFlow<String> = _segmentFilter.asStateFlow()

  private val _statusFilter = MutableStateFlow("Todos")
  val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

  private val _scoreStartFilter = MutableStateFlow("")
  val scoreStartFilter: StateFlow<String> = _scoreStartFilter.asStateFlow()

  private val _scoreEndFilter = MutableStateFlow("")
  val scoreEndFilter: StateFlow<String> = _scoreEndFilter.asStateFlow()

  private val _filteredClients = MutableStateFlow<List<User>>(emptyList())
  val filteredClients: StateFlow<List<User>> = _filteredClients.asStateFlow()

  init {
    loadCampaigns()
  }

  private fun loadCampaigns() {
    viewModelScope.launch {
      campaignRepository.getCampaigns(
        onSuccess = {
          _allCampaigns.value = it
          filterCampaigns()
        },
        onFailure = {
          // Handle error
        }
      )
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    filterCampaigns()
  }

  private fun filterCampaigns() {
    val query = _searchQuery.value.lowercase()

    val filtered = _allCampaigns.value.filter { campaign ->
      val nameMatches = campaign.title.lowercase().contains(query)
      nameMatches
    }

    _filteredCampaigns.value = filtered
  }

  fun onNewCampaignTitleChange(title: String) {
    _newCampaignTitle.value = title
  }

  fun onNewCampaignDescriptionChange(description: String) {
    _newCampaignDescription.value = description
  }

  fun onNewCampaignStartDateChange(date: String) {
    _newCampaignStartDate.value = date
  }

  fun onNewCampaignEndDateChange(date: String) {
    _newCampaignEndDate.value = date
  }

  fun onSegmentFilterChange(segment: String) {
    _segmentFilter.value = segment
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

  fun sendCampaign() {
    val campaign = Campaign(
      title = _newCampaignTitle.value,
      description = _newCampaignDescription.value,
      startDate = _newCampaignStartDate.value,
      endDate = _newCampaignEndDate.value
    )
    campaignRepository.sendCampaign(campaign, onSuccess = { loadCampaigns() }, onFailure = {})
  }

  fun getFilteredClients() {
    viewModelScope.launch {
      val allUsers = authRepository.getUsers()
      val allCostumers = allUsers.filter { it.role == "Cliente" }
      val segmentFilter = _segmentFilter.value
      val statusFilter = _statusFilter.value

      val filtered = allCostumers.filter { user ->
        val segmentMatches = when (segmentFilter) {
          "Todos" -> true
          else -> user.segment.equals(segmentFilter, ignoreCase = true)
        }
        val statusMatches = when (statusFilter) {
          "Todos" -> true
          else -> user.status.equals(statusFilter, ignoreCase = true)
        }

        val scoreStart = _scoreStartFilter.value.toIntOrNull() ?: 0
        val scoreEnd = _scoreEndFilter.value.toIntOrNull() ?: Int.MAX_VALUE
        val scoreMatches = user.score in scoreStart..scoreEnd

        segmentMatches && statusMatches && scoreMatches
      }

      _filteredClients.value = filtered
    }
  }
}
