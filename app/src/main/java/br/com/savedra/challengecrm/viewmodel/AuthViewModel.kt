package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.navigation.AppRoutes
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUIState {
  object Idle : AuthUIState()
  object Loading : AuthUIState()
  data class Success(val role: String?) : AuthUIState()
  data class Error(val message: String) : AuthUIState()
}

class AuthViewModel : ViewModel() {
  private val authRepository: AuthRepository

  init {
    val auth = Firebase.auth
    val firestore = Firebase.firestore

    authRepository = AuthRepository(auth, firestore)
  }

  private val _authUiState = MutableStateFlow<AuthUIState>(AuthUIState.Idle)

  val authUiState = _authUiState

  private val _email = MutableStateFlow("")
  val email = _email.asStateFlow()

  private val _password = MutableStateFlow("")
  val password = _password.asStateFlow()

  private val _name = MutableStateFlow("")
  val name = _name.asStateFlow()

  private val _role = MutableStateFlow("")
  val role = _role.asStateFlow()

  private val _state = MutableStateFlow("")
  val state = _state.asStateFlow()

  private val _segment = MutableStateFlow("")
  val segment = _segment.asStateFlow()

  fun onEmailChange(newEmail: String) {
    _email.value = newEmail
  }

  fun onPasswordChange(newPassword: String) {
    _password.value = newPassword
  }

  fun onNameChange(newName: String) {
    _name.value = newName
  }

  fun onRoleChange(newRole: String) {
    _role.value = newRole
  }

  fun onEstadoChange(newState: String) {
    _state.value = newState
  }

  fun onSegmentChange(newSegment: String) {
    _segment.value = newSegment
  }

  fun login() {
    val email = _email.value
    val password = _password.value

    if (email.isBlank() || password.isBlank()) {
      _authUiState.value = AuthUIState.Error("Preencha todos os campos.")
      return
    }

    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        authRepository.login(email, password)
        val user = authRepository.getCurrentUser()
        val role = user?.let { authRepository.getUserRole(it.uid) }
        _authUiState.value = AuthUIState.Success(role)
      } catch (e: Exception) {
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun register() {
    val name = _name.value
    val email = _email.value
    val password = _password.value
    val role = _role.value
    val state = _state.value
    val segment = _segment.value

    if (email.isBlank() || password.isBlank() || name.isBlank()) {
      _authUiState.value = AuthUIState.Error("Preencha todos os campos.")
      return
    }

    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        authRepository.register(email, password, name, role, state, segment)
        _authUiState.value = AuthUIState.Success(role)
      } catch (e: Exception) {
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _authUiState.value = AuthUIState.Idle
  }
}