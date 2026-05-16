package br.com.savedra.challengecrm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.AuthRepository
import br.com.savedra.challengecrm.di.RepositoryProvider
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUIState {
  object Idle : AuthUIState()
  object Loading : AuthUIState()
  data class Success(val user: User?, val role: String?) : AuthUIState()
  data class Error(val message: String) : AuthUIState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
  private val authRepository: AuthRepository

  init {
    Log.d("AuthDebug", "AuthViewModel 'init' block called. Instance created.")
    authRepository = RepositoryProvider.getAuthRepository(application)
  }

  private val _authUiState = MutableStateFlow<AuthUIState>(AuthUIState.Idle)

  val authUiState = _authUiState.asStateFlow()

  private val _currentUser = MutableStateFlow<User?>(null)
  val currentUser = _currentUser.asStateFlow()

  fun performInitialAuthCheck() {
    // Se um usuário já foi carregado, não faz nada
    if (_currentUser.value != null) {
        Log.d("AuthDebug", "performInitialAuthCheck() - Check skipped, user already loaded.")
        return
    }

    viewModelScope.launch {
      try {
        // Tenta obter os dados do usuário que já pode estar logado
        Log.d("AuthDebug", "performInitialAuthCheck() - Attempting to get current user data.")
        val user = authRepository.getCurrentUserData()
        if (user != null) {
          _currentUser.value = user
          _authUiState.value = AuthUIState.Success(user, user.role)
          Log.d("AuthDebug", "performInitialAuthCheck() - User found and loaded: ${user.email}")
        } else {
          _authUiState.value = AuthUIState.Idle // Garante que o estado seja Idle se não houver usuário
          Log.d("AuthDebug", "performInitialAuthCheck() - No authenticated user found.")
        }
      } catch (e: Exception) {
        // Erro ao buscar dados do usuário, pode ser que não esteja logado
        Log.e("AuthDebug", "performInitialAuthCheck() - Error checking current user", e)
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro ao checar usuário")
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

    Log.d("AuthDebug", "login() - Starting login for email: $email")
    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        authRepository.login(email, password)
        val user = authRepository.getCurrentUserData()
        Log.d("AuthDebug", "login() - User data fetched: $user")

        _currentUser.value = user
        Log.d("AuthDebug", "login() - _currentUser state updated.")

        _authUiState.value = AuthUIState.Success(user, user?.role)
        Log.d("AuthDebug", "login() - _authUiState updated to Success.")

      } catch (e: Exception) {
        Log.e("AuthDebug", "login() - Error during login", e)
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
        _authUiState.value = AuthUIState.Success(user, user?.role)
      } catch (e: Exception) {
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro desconhecido")
      }
    }
  }

  fun resetUiState() {
    _authUiState.value = AuthUIState.Idle
  }

  fun logout() {
    Log.d("AuthDebug", "logout() - Starting logout process.")
    viewModelScope.launch {
      try {
        // Efetua o logout no repositório (que chama o Firebase Auth)
        authRepository.logout()
        // Limpa o estado do usuário atual na ViewModel
        _currentUser.value = null
        // Reseta o estado da UI para o estado inicial
        _authUiState.value = AuthUIState.Idle
        Log.d("AuthDebug", "logout() - Local state cleared (_currentUser is null, _authUiState is Idle).")
      } catch (e: Exception) {
        Log.e("AuthDebug", "logout() - Error during logout", e)
        // Mesmo em caso de erro, tenta limpar o estado local
        _currentUser.value = null
        _authUiState.value = AuthUIState.Error(e.message ?: "Erro ao deslogar")
      }
    }
  }
}