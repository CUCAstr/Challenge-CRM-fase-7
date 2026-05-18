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
import android.util.Log

class UsersViewModel(application: Application) : AndroidViewModel(application) {
  private val authRepository = RepositoryProvider.getAuthRepository(application)
  private val customerRepository = RepositoryProvider.getCustomerRepository(application)

  private val _users = MutableStateFlow<List<User>>(emptyList())
  val users: StateFlow<List<User>> = _users.asStateFlow()

  init {
    loadUsers()
  }

  /**
   * Carrega todos os usuários do sistema.
   */
  fun loadUsers() {
    viewModelScope.launch {
      try {
        Log.d("UsersVM", "--- INICIANDO CARGA DE USUÁRIOS ---")
        
        // 1. Tentar carregar via CustomerApi (Lista principal de clientes)
        val customers = try {
            customerRepository.getCustomers()
        } catch (e: Exception) {
            Log.e("UsersVM", "Falha no CustomerRepo: ${e.message}")
            emptyList()
        }

        // 2. Tentar carregar via AuthApi (Lista de segurança)
        val authUsers = try {
            authRepository.getUsers()
        } catch (e: Exception) {
            Log.e("UsersVM", "Falha no AuthRepo: ${e.message}")
            emptyList()
        }

        // 3. Merge das listas (Removendo duplicatas por ID)
        val combined = (customers + authUsers).distinctBy { it.id }.filter { !it.id.isNullOrBlank() }
        
        _users.value = combined
        Log.d("UsersVM", "CARGA FINALIZADA: ${combined.size} usuários únicos encontrados.")
        combined.forEach { Log.d("UsersVM", " - User: ${it.name} | Role: ${it.role} | ID: ${it.id}") }
        
      } catch (e: Exception) {
        Log.e("UsersVM", "ERRO CRÍTICO no loadUsers", e)
      }
    }
  }
}
