package br.com.savedra.challengecrm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.di.RepositoryProvider
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {
  private val authRepository = RepositoryProvider.getAuthRepository(application)

  private val _users = MutableStateFlow<List<User>>(emptyList())
  val users: StateFlow<List<User>> = _users.asStateFlow()

  init {
    loadUsers()
  }

  /**
   * Carrega todos os usuários do sistema via repositório.
   * Tornada pública para permitir recarregamento manual pela UI.
   */
  fun loadUsers() {
    viewModelScope.launch {
      try {
        _users.value = authRepository.getUsers()
      } catch (e: Exception) {
        android.util.Log.e("UsersVM", "Erro ao carregar usuários (Offline?)", e)
      }
    }
  }
}
