package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.data.repository.PromotionRepository
import br.com.savedra.challengecrm.model.Promotion
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PromotionViewModel : ViewModel() {

  private val promotionRepository = PromotionRepository()
  private val authRepository =
    AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _allPromotions = MutableStateFlow<List<Promotion>>(emptyList())

  private val _filteredPromotions = MutableStateFlow<List<Promotion>>(emptyList())
  val filteredPromotions: StateFlow<List<Promotion>> = _filteredPromotions.asStateFlow()

  private val _newPromotionTitle = MutableStateFlow("")
  val newPromotionTitle: StateFlow<String> = _newPromotionTitle.asStateFlow()

  private val _newPromotionDescription = MutableStateFlow("")
  val newPromotionDescription: StateFlow<String> = _newPromotionDescription.asStateFlow()

  private val _newPromotionOriginalValue = MutableStateFlow("")
  val newPromotionOriginalValue: StateFlow<String> = _newPromotionOriginalValue.asStateFlow()

  private val _newPromotionPromotionValue = MutableStateFlow("")
  val newPromotionPromotionValue: StateFlow<String> = _newPromotionPromotionValue.asStateFlow()

  private val _newPromotionDateExpiresIn = MutableStateFlow("")
  val newPromotionDateExpiresIn: StateFlow<String> = _newPromotionDateExpiresIn.asStateFlow()

  private val _newPromotionHoursExpiresIn = MutableStateFlow("")
  val newPromotionHoursExpiresIn: StateFlow<String> = _newPromotionHoursExpiresIn.asStateFlow()

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
    loadPromotions()
  }

  private fun loadPromotions() {
    viewModelScope.launch {
      promotionRepository.getPromotions(
        userSegment = null,
        onSuccess = {
          _allPromotions.value = it
          filterPromotions()
        },
        onFailure = {
          // Handle error
        }
      )
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    filterPromotions()
  }

  private fun filterPromotions() {
    val query = _searchQuery.value.lowercase()

    val filtered = _allPromotions.value.filter { promotion ->
      val nameMatches = promotion.title.lowercase().contains(query)
      nameMatches
    }

    _filteredPromotions.value = filtered
  }

  fun onNewPromotionTitleChange(title: String) {
    _newPromotionTitle.value = title
  }

  fun onNewPromotionDescriptionChange(description: String) {
    _newPromotionDescription.value = description
  }

  fun onNewPromotionOriginalValueChange(originalValue: String) {
    _newPromotionOriginalValue.value = originalValue
  }

  fun onNewPromotionPromotionValueChange(promotionValue: String) {
    _newPromotionPromotionValue.value = promotionValue
  }

  fun onNewPromotionDateExpiresInChange(date: String) {
    _newPromotionDateExpiresIn.value = date
  }

  fun onNewPromotionHoursExpiresInChange(hours: String) {
    _newPromotionHoursExpiresIn.value = hours
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

  fun sendPromotion(onSuccess: () -> Unit) {
    if (
      _newPromotionTitle.value.isBlank() ||
      _newPromotionDescription.value.isBlank() ||
      _newPromotionOriginalValue.value.isBlank() ||
      _newPromotionPromotionValue.value.isBlank() ||
      _newPromotionDateExpiresIn.value.isBlank() ||
      _newPromotionHoursExpiresIn.value.isBlank()
    ) {
      _showError.value = true
      return
    }

    val promotion = Promotion(
      title = _newPromotionTitle.value,
      description = _newPromotionDescription.value,
      originalValue = _newPromotionOriginalValue.value,
      promotionValue = _newPromotionPromotionValue.value,
      dateExpiresIn = _newPromotionDateExpiresIn.value,
      hoursExpiresIn = _newPromotionHoursExpiresIn.value
    )
    promotionRepository.sendPromotion(promotion, onSuccess = {
      loadPromotions()
      _newPromotionTitle.value = ""
      _newPromotionDescription.value = ""
      _newPromotionOriginalValue.value = ""
      _newPromotionPromotionValue.value = ""
      _newPromotionDateExpiresIn.value = ""
      _newPromotionHoursExpiresIn.value = ""
      _showError.value = false
      onSuccess()
    }, onFailure = {})
  fun sendPromotion() {
    viewModelScope.launch {
        val promotion = Promotion(
          title = _newPromotionTitle.value,
          description = _newPromotionDescription.value,
          originalValue = _newPromotionOriginalValue.value,
          promotionValue = _newPromotionPromotionValue.value,
          dateExpiresIn = _newPromotionDateExpiresIn.value,
          hoursExpiresIn = _newPromotionHoursExpiresIn.value,
          segment = _segmentFilter.value
        )
        promotionRepository.sendPromotion(promotion)
        loadPromotions()
        clearNewPromotionFields()
    }
  }

  fun clearNewPromotionFields() {
    _newPromotionTitle.value = ""
    _newPromotionDescription.value = ""
    _newPromotionOriginalValue.value = ""
    _newPromotionPromotionValue.value = ""
    _newPromotionDateExpiresIn.value = ""
    _newPromotionHoursExpiresIn.value = ""
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
