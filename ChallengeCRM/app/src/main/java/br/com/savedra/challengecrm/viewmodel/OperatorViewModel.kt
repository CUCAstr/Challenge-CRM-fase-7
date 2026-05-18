package br.com.savedra.challengecrm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.CustomerRepository
import br.com.savedra.challengecrm.di.RepositoryProvider
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import kotlinx.coroutines.launch

/**
 * ViewModel para a Gestão de Clientes (Operador).
 */
class OperatorViewModel(application: Application) : AndroidViewModel(application) {
  private val customerRepository = RepositoryProvider.getCustomerRepository(application)

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _segmentFilter = MutableStateFlow("Todos")
  private val _statusFilter = MutableStateFlow("Todos")

  private val _allCustomers = MutableStateFlow<List<User>>(emptyList())

  private val _filteredCustomers = MutableStateFlow<List<User>>(emptyList())
  val filteredCustomers: StateFlow<List<User>> = _filteredCustomers.asStateFlow()

  init {
    loadCustomers()
  }

  /**
   * Carrega lista de clientes do backend.
   */
  fun loadCustomers() {
    viewModelScope.launch {
      try {
        val customers = customerRepository.getCustomers()
        _allCustomers.value = customers
        filterCustomers()
        Log.d("OperatorVM", "Clientes carregados: ${customers.size}")
      } catch (e: Exception) {
        Log.e("OperatorVM", "Erro ao carregar clientes", e)
      }
    }
  }

  fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    filterCustomers()
  }

  fun updateSegmentFilter(filter: String) {
    _segmentFilter.value = filter
    filterCustomers()
  }

  fun updateStatusFilter(filter: String) {
    _statusFilter.value = filter
    filterCustomers()
  }

  private fun filterCustomers() {
    val query = _searchQuery.value.lowercase()
    val segmentF = _segmentFilter.value
    val statusF = _statusFilter.value

    val filtered = _allCustomers.value.filter { customer ->
      val nameMatches = (customer.name ?: "").lowercase().contains(query)

      val segmentMatches = when (segmentF) {
        "Todos" -> true
        else -> (customer.segment ?: "").equals(segmentF, ignoreCase = true)
      }

      val statusMatches = when (statusF) {
        "Todos" -> true
        else -> (customer.status ?: "").equals(statusF, ignoreCase = true)
      }

      nameMatches && segmentMatches && statusMatches
    }

    _filteredCustomers.value = filtered
  }

  /**
   * Salva notas do operador sobre um cliente.
   * 
   * CORREÇÃO: Garante que a nota seja salva e a lista local atualizada.
   */
  fun saveNotes(userId: String, notes: String) {
    if (userId.isBlank()) return
    viewModelScope.launch {
      try {
        Log.d("OperatorVM", "Salvando notas para o usuário: $userId")
        val customer = customerRepository.getCustomerById(userId)
        if (customer != null) {
            val updated = customer.copy(notes = notes)
            val result = customerRepository.updateCustomer(userId, updated)
            if (result != null) {
                Log.d("OperatorVM", "Notas salvas com sucesso no backend")
                loadCustomers() // Recarrega lista para refletir mudança
            }
        }
      } catch (e: Exception) {
        Log.e("OperatorVM", "Falha ao salvar notas", e)
      }
    }
  }
}
