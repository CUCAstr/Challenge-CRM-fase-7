package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.InviteRepository
import br.com.savedra.challengecrm.model.Invite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InviteViewModel : ViewModel() {

    private val inviteRepository = InviteRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allInvites = MutableStateFlow<List<Invite>>(emptyList())

    private val _filteredInvites = MutableStateFlow<List<Invite>>(emptyList())
    val filteredInvites: StateFlow<List<Invite>> = _filteredInvites.asStateFlow()

    init {
        loadInvites()
    }

    private fun loadInvites() {
        viewModelScope.launch {
            inviteRepository.getInvites(
                onSuccess = {
                    _allInvites.value = it
                    filterInvites()
                },
                onFailure = {
                    // Handle error
                }
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterInvites()
    }

    private fun filterInvites() {
        val query = _searchQuery.value.lowercase()

        val filtered = _allInvites.value.filter { invite ->
            val nameMatches = invite.name.lowercase().contains(query)
            nameMatches
        }

        _filteredInvites.value = filtered
    }
}