package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.data.repository.BannerRepository
import br.com.savedra.challengecrm.model.Banner
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BannerViewModel : ViewModel() {

  private val bannerRepository = BannerRepository()
  private val authRepository =
    AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _allBanners = MutableStateFlow<List<Banner>>(emptyList())

  private val _filteredBanners = MutableStateFlow<List<Banner>>(emptyList())
  val filteredBanners: StateFlow<List<Banner>> = _filteredBanners.asStateFlow()

  private val _newBannerTitle = MutableStateFlow("")
  val newBannerTitle: StateFlow<String> = _newBannerTitle.asStateFlow()

  private val _newBannerDescription = MutableStateFlow("")
  val newBannerDescription: StateFlow<String> = _newBannerDescription.asStateFlow()

  private val _newBannerImageUrl = MutableStateFlow("")
  val newBannerImageUrl: StateFlow<String> = _newBannerImageUrl.asStateFlow()

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

  fun loadBanners(userSegment: String? = null) {
    viewModelScope.launch {
        try {
            _allBanners.value = bannerRepository.getBanners(userSegment)
            filterBanners()
        } catch (e: Exception) {
            // Handle error
        }
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    filterBanners()
  }

  private fun filterBanners() {
    val query = _searchQuery.value.lowercase()

    val filtered = _allBanners.value.filter { banner ->
      val nameMatches = banner.title.lowercase().contains(query)
      nameMatches
    }

    _filteredBanners.value = filtered
  }

  fun onNewBannerTitleChange(title: String) {
    _newBannerTitle.value = title
  }

  fun onNewBannerDescriptionChange(description: String) {
    _newBannerDescription.value = description
  }

  fun onNewBannerImageUrlChange(imageUrl: String) {
    _newBannerImageUrl.value = imageUrl
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

  private val _showError = MutableStateFlow(false)
  val showError: StateFlow<Boolean> = _showError.asStateFlow()

  fun sendBanner() {
    if (_newBannerTitle.value.isBlank() || _newBannerDescription.value.isBlank() || _newBannerImageUrl.value.isBlank()) {
      _showError.value = true
      return
    }

    viewModelScope.launch {
        val banner = Banner(
          title = _newBannerTitle.value,
          description = _newBannerDescription.value,
          imageUrl = _newBannerImageUrl.value,
          segment = _segmentFilter.value
        )
        bannerRepository.sendBanner(banner)
        loadBanners()
        clearNewBannerFields()
    }
  }

  fun clearNewBannerFields() {
    _newBannerTitle.value = ""
    _newBannerDescription.value = ""
    _newBannerImageUrl.value = ""
    _segmentFilter.value = "Todos"
    _statusFilter.value = "Todos"
    _scoreStartFilter.value = ""
    _scoreEndFilter.value = ""
    _filteredClients.value = emptyList()
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
