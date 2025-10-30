package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.SheratonHotelRepository
import br.com.savedra.challengecrm.model.SheratonHotel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class SheratonHotelUIState {
  object Idle : SheratonHotelUIState()
  object Loading : SheratonHotelUIState()
  object Success : SheratonHotelUIState()
  data class Error(val message: String) : SheratonHotelUIState()
}

class SheratonHotelViewModel : ViewModel() {
  private val sheratonHotelRepository: SheratonHotelRepository

  init {
    val firestore = Firebase.firestore
    sheratonHotelRepository = SheratonHotelRepository(firestore)
  }

  private val _sheratonUiState = MutableStateFlow<SheratonHotelUIState>(SheratonHotelUIState.Idle)
  val sheratonUiState = _sheratonUiState

  fun saveSheratonHotel(sheratonHotel: SheratonHotel) {
    if (sheratonHotel.checkInDate.isBlank() || sheratonHotel.checkOutDate.isBlank() || sheratonHotel.guests.isBlank() || sheratonHotel.roomType.isBlank()) {
      _sheratonUiState.value = SheratonHotelUIState.Error("Todos os campos devem ser preenchidos.")
      return
    }

    _sheratonUiState.value = SheratonHotelUIState.Loading
    viewModelScope.launch {
      try {
        sheratonHotelRepository.saveSheratonHotel(sheratonHotel)
        _sheratonUiState.value = SheratonHotelUIState.Success
      } catch (e: Exception) {
        _sheratonUiState.value = SheratonHotelUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _sheratonUiState.value = SheratonHotelUIState.Idle
  }
}