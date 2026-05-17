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

/**
 * Estados da interface de autenticação.
 */
sealed class AuthUIState {
  object Idle : AuthUIState()
  object Loading : AuthUIState()
  data class Success(val user: User?, val role: String?) : AuthUIState()
  data class Error(val message: String) : AuthUIState()
}

/**
 * ViewModel responsável pela lógica de Autenticação e Registro.
 * Gerencia o estado dos campos de entrada e a comunicação com o repositório.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {
  private val authRepository: AuthRepository

  init {
    Log.d("AuthDebug", "AuthViewModel 'init' block called. Instance created.")
    authRepository = RepositoryProvider.getAuthRepository(application)
  }

  // --- ESTADOS DE UI (UI States) ---
  private val _authUiState = MutableStateFlow<AuthUIState>(AuthUIState.Idle)
  val authUiState = _authUiState.asStateFlow()

  private val _currentUser = MutableStateFlow<User?>(null)
  val currentUser = _currentUser.asStateFlow()

  // --- ESTADOS DE ENTRADA (Input States) ---
  // Utilizamos MutableStateFlow para manter os dados reativos que o Compose observa.
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

  private val _role = MutableStateFlow("Cliente")
  val role = _role.asStateFlow()

  /**
   * Reseta todos os campos de entrada e mensagens de erro.
   * Chamado ao navegar entre Login e Cadastro para limpar dados residuais.
   */
  fun resetUiState() {
    Log.d("AuthDebug", "resetUiState() - Limpando campos e erros.")
    _email.value = ""
    _password.value = ""
    _name.value = ""
    _company.value = ""
    _segment.value = ""
    _gender.value = ""
    _phone.value = ""
    _role.value = "Cliente"
    _authUiState.value = AuthUIState.Idle
  }

  // --- MÉTODOS DE ATUALIZAÇÃO (Update Methods) ---
  fun onEmailChange(newEmail: String) { _email.value = newEmail }
  fun onPasswordChange(newPassword: String) { _password.value = newPassword }
  fun onNameChange(newName: String) { _name.value = newName }
  fun onCompanyChange(newCompany: String) { _company.value = newCompany }
  fun onSegmentChange(newSegment: String) { _segment.value = newSegment }
  fun onGenderChange(newGender: String) { _gender.value = newGender }
  fun onPhoneChange(newPhone: String) { _phone.value = newPhone }
  fun onRoleChange(newRole: String) { _role.value = newRole }

  /**
   * Verifica se o usuário já possui uma sessão ativa ao iniciar o app.
   */
  fun performInitialAuthCheck() {
    if (_currentUser.value != null) return

    viewModelScope.launch {
      try {
        val user = authRepository.getCurrentUserData()
        if (user != null) {
          val displayRole = if (user.role == "OPERATOR") "Operador" else "Cliente"
          val updatedUser = user.copy(role = displayRole)
          _currentUser.value = updatedUser
          _authUiState.value = AuthUIState.Success(updatedUser, displayRole)
        } else {
          _authUiState.value = AuthUIState.Idle
        }
      } catch (e: Exception) {
        Log.e("AuthDebug", "Erro no check inicial", e)
        _authUiState.value = AuthUIState.Idle
      }
    }
  }

  /**
   * Realiza a autenticação no backend.
   */
  fun login() {
    val emailVal = _email.value
    val passVal = _password.value

    if (emailVal.isBlank() || passVal.isBlank()) {
      _authUiState.value = AuthUIState.Error("Email e senha são obrigatórios.")
      return
    }

    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        authRepository.login(emailVal, passVal)
        val user = authRepository.getCurrentUserData()
        
        val displayRole = if (user?.role == "OPERATOR") "Operador" else "Cliente"
        val updatedUser = user?.copy(role = displayRole)

        _currentUser.value = updatedUser
        _authUiState.value = AuthUIState.Success(updatedUser, displayRole)

      } catch (e: Exception) {
        Log.e("AuthDebug", "login() - Erro", e)
        
        // --- CORREÇÃO: EXTRAÇÃO DE MENSAGEM ---
        // Se o servidor retornar um JSON de erro (como o status 500 que vimos),
        // tentamos extrair apenas a mensagem amigável.
        val rawMessage = e.message ?: "Falha na autenticação."
        val friendlyMessage = if (rawMessage.contains("Internal Server Error")) {
            "Erro interno no servidor. Tente novamente mais tarde."
        } else if (rawMessage.contains("403")) {
            "Acesso negado ou credenciais incorretas."
        } else {
            rawMessage
        }
        
        _authUiState.value = AuthUIState.Error(friendlyMessage)
      }
    }
  }

  /**
   * Realiza o cadastro de um novo usuário.
   */
  fun register() {
    val nameVal = _name.value
    val emailVal = _email.value
    val passwordVal = _password.value
    val companyVal = _company.value
    val roleVal = _role.value
    val segmentVal = _segment.value
    val genderVal = _gender.value
    val phoneVal = _phone.value
    val categoryVal = "Básico"

    if (emailVal.isBlank() || passwordVal.isBlank() || nameVal.isBlank()) {
      _authUiState.value = AuthUIState.Error("Preencha Nome, Email e Senha.")
      return
    }

    _authUiState.value = AuthUIState.Loading
    viewModelScope.launch {
      try {
        val mappedRole = if (roleVal == "Operador") "OPERATOR" else "CLIENT"
        authRepository.register(
          emailVal, passwordVal, nameVal, companyVal, mappedRole, 
          segmentVal, genderVal, phoneVal, categoryVal
        )
        
        val user = authRepository.getCurrentUserData()
        val displayRole = if (user?.role == "OPERATOR") "Operador" else "Cliente"
        val updatedUser = user?.copy(role = displayRole)
        
        _currentUser.value = updatedUser
        _authUiState.value = AuthUIState.Success(updatedUser, displayRole)
      } catch (e: Exception) {
        Log.e("AuthDebug", "register() - Erro", e)
        
        // --- CORREÇÃO: EXTRAÇÃO DE MENSAGEM ---
        val rawMessage = e.message ?: "Falha no cadastro."
        val friendlyMessage = if (rawMessage.contains("Internal Server Error")) {
            "Erro interno no servidor. Verifique se o email já existe."
        } else if (rawMessage.contains("403")) {
            "Acesso negado. Tente outro email."
        } else {
            rawMessage
        }
        
        _authUiState.value = AuthUIState.Error(friendlyMessage)
      }
    }
  }

  /**
   * Encerra a sessão do usuário.
   */
  fun logout() {
    viewModelScope.launch {
      try {
        authRepository.logout()
        _currentUser.value = null
        _authUiState.value = AuthUIState.Idle
        resetUiState()
      } catch (e: Exception) {
        Log.e("AuthDebug", "logout() - Erro", e)
        _currentUser.value = null
        _authUiState.value = AuthUIState.Idle
      }
    }
  }
}
