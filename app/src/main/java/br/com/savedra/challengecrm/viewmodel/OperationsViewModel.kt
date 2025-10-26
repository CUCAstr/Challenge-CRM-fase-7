package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OperationsViewModel : ViewModel() {

    private val _vipFilter = MutableStateFlow("Todos")
    val vipFilter: StateFlow<String> = _vipFilter.asStateFlow()

    private val _stateFilter = MutableStateFlow("Todos")
    val stateFilter: StateFlow<String> = _stateFilter.asStateFlow()

    fun updateVipFilter(filter: String) {
        _vipFilter.value = filter
    }

    fun updateStateFilter(filter: String) {
        _stateFilter.value = filter
    }
}