package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.BusinessClubRepository
import br.com.savedra.challengecrm.model.BusinessClub
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class BusinessClubUIState {
  object Idle : BusinessClubUIState()
  object Loading : BusinessClubUIState()
  object Success : BusinessClubUIState()
  data class Error(val message: String) : BusinessClubUIState()
}

class BusinessClubViewModel : ViewModel() {
  private val businessClubRepository: BusinessClubRepository

  init {
    val firestore = Firebase.firestore
    businessClubRepository = BusinessClubRepository(firestore)
  }

  private val _businessUiState = MutableStateFlow<BusinessClubUIState>(BusinessClubUIState.Idle)
  val businessUiState = _businessUiState

  fun saveBusinessClub(businessClub: BusinessClub) {
    if (businessClub.reason.isBlank()) {
      _businessUiState.value = BusinessClubUIState.Error("O campo de motivo deve ser preenchido.")
      return
    }

    _businessUiState.value = BusinessClubUIState.Loading
    viewModelScope.launch {
      try {
        businessClubRepository.saveBusinessClub(businessClub)
        _businessUiState.value = BusinessClubUIState.Success
      } catch (e: Exception) {
        _businessUiState.value = BusinessClubUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _businessUiState.value = BusinessClubUIState.Idle
  }
}