package br.com.savedra.challengecrm.viewmodel

import android.util.Log
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

import br.com.savedra.challengecrm.model.User

sealed class AuthUIState {
  object Idle : AuthUIState()
  object Loading : AuthUIState()
  data class Success(val user: User?) : AuthUIState()
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

  val authUiState = _authUiState.asStateFlow()

  private val _currentUser = MutableStateFlow<User?>(null)
  val currentUser = _currentUser.asStateFlow()

  init {
    // Verifica o estado de autenticação na inicialização do ViewModel
    checkCurrentUser()
  }

  private fun checkCurrentUser() {
    viewModelScope.launch {
      try {
        // Tenta obter os dados do usuário que já pode estar logado
        val user = authRepository.getCurrentUserData()
        if (user != null) {
          _currentUser.value = user
          _authUiState.value = AuthUIState.Success(user) // Opcional: atualizar o estado da UI
          Log.d("AuthViewModel", "Usuário já logado encontrado: ${user.email}")
        }
      } catch (e: Exception) {
        // Erro ao buscar dados do usuário, pode ser que não esteja logado
        Log.e("AuthViewModel", "Erro ao verificar usuário atual", e)
      }
    }
  }

  private val _email = MutableStateFlow("")
  val email = _email.asStateFlow()

  private val _password = MutableStateFlow("")
  val password = _password.asStateFlow()

  private val _name = MutableStateFlow("")
  val name = _name.asStateFlow()

  private val _company = MutableStateFlow("")
  val company = _company.asStateFlow()

  private val _segment = MutableStateFlow("")
  val segment = _segment.asStateFlow()

  private val _gender = MutableStateFlow("")
  val gender = _gender.asStateFlow()

  private val _phone = MutableStateFlow("")
  val phone = _phone.asStateFlow()

  fun onEmailChange(newEmail: String) {
    _email.value = newEmail
  }

  fun onPasswordChange(newPassword: String) {
    _password.value = newPassword
  }

  fun onNameChange(newName: String) {
    _name.value = newName
  }

  fun onCompanyChange(newCompany: String) {
    _company.value = newCompany
  }

  fun onSegmentChange(newSegment: String) {
    _segment.value = newSegment
  }

  fun onGenderChange(newGender: String) {
    _gender.value = newGender
  }

  fun onPhoneChange(newPhone: String) {
    _phone.value = newPhone
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
        val user = authRepository.getCurrentUserData()
        _currentUser.value = user
        _authUiState.value = AuthUIState.Success(user)
      } catch (e: Exception) {
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun register() {
    val name = _name.value
    val email = _email.value
    val password = _password.value
    val company = _company.value
    val role = "Cliente"
    val segment = _segment.value
    val gender = _gender.value
    val phone = _phone.value
    val category = "Básico"

    if (email.isBlank() || password.isBlank() || name.isBlank() || company.isBlank() ||
      segment.isBlank() || gender.isBlank() || phone.isBlank()
    ) {
      _authUiState.value = AuthUIState.Error("Preencha todos os campos.")
      return
    }

    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        authRepository.register(
          email,
          password,
          name,
          company,
          role,
          segment,
          gender,
          phone,
          category
        )
        val user = authRepository.getCurrentUserData()
        _currentUser.value = user
        _authUiState.value = AuthUIState.Success(user)
      } catch (e: Exception) {
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _authUiState.value = AuthUIState.Idle
  }
}