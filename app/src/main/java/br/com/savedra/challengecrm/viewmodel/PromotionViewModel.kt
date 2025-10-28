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
  private val authRepository = AuthRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())

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

  private val _filteredClients = MutableStateFlow<List<User>>(emptyList())
  val filteredClients: StateFlow<List<User>> = _filteredClients.asStateFlow()

  init {
    loadPromotions()
  }

  private fun loadPromotions() {
    viewModelScope.launch {
      promotionRepository.getPromotions(
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

  fun sendPromotion() {
    val promotion = Promotion(
      title = _newPromotionTitle.value,
      description = _newPromotionDescription.value,
      originalValue = _newPromotionOriginalValue.value,
      promotionValue = _newPromotionPromotionValue.value,
      dateExpiresIn = _newPromotionDateExpiresIn.value,
      hoursExpiresIn = _newPromotionHoursExpiresIn.value
    )
    promotionRepository.sendPromotion(promotion, onSuccess = { loadPromotions() }, onFailure = {})
  }

  fun getFilteredClients() {
    viewModelScope.launch {
      val allUsers = authRepository.getUsers()
      val allCostumers = allUsers.filter { it.role == "Cliente" }
      val segmentFilter = _segmentFilter.value

      val filtered = allCostumers.filter { user ->
        val segmentMatches = when (segmentFilter) {
          "Todos" -> true
          else -> user.segment.equals(segmentFilter, ignoreCase = true)
        }
        segmentMatches
      }

      _filteredClients.value = filtered
    }
  }
}
