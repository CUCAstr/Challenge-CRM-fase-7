package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.EventRepository
import br.com.savedra.challengecrm.model.EventsCenter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class EventUIState {
  object Idle : EventUIState()
  object Loading : EventUIState()
  object Success : EventUIState()
  data class Error(val message: String) : EventUIState()
}

class EventViewModel : ViewModel() {
  private val eventRepository: EventRepository

  init {
    val firestore = Firebase.firestore
    eventRepository = EventRepository(firestore)
  }

  private val _eventUiState = MutableStateFlow<EventUIState>(EventUIState.Idle)
  val eventUiState = _eventUiState

  fun saveEvent(event: EventsCenter) {
    if (event.eventName.isBlank() || event.eventDate.isBlank() ||
        event.segment.isBlank() || event.numberOfPeople.isBlank() || event.eventSetup.isBlank()
        || event.audienceProfile.isBlank() || event.eventPeriodicity.isBlank()
        || event.competingWith.isBlank() || event.budget.isBlank() ||
        event.numberOfApartments.isBlank() || event.decisiveFactor.isBlank()
        || event.necessaryDifferential.isBlank()) {
      _eventUiState.value = EventUIState.Error("Todos os campos devem ser preenchidos.")
      return
    }

    _eventUiState.value = EventUIState.Loading
    viewModelScope.launch {
      try {
        eventRepository.saveEvent(event)
        _eventUiState.value = EventUIState.Success
      } catch (e: Exception) {
        _eventUiState.value = EventUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _eventUiState.value = EventUIState.Idle
  }
}