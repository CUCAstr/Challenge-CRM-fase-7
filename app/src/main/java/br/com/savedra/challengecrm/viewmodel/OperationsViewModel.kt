package br.com.savedra.challengecrm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.savedra.challengecrm.data.repository.CampaignRepository
import br.com.savedra.challengecrm.model.Campaign
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OperationsViewModel(private val customerViewModel: CustomerViewModel) : ViewModel() {

    private val campaignRepository = CampaignRepository(FirebaseFirestore.getInstance())

    private val _startDateString = MutableStateFlow("")
    val startDateString = _startDateString.asStateFlow()

    private val _startDateMillis = MutableStateFlow<Long?>(null)
    val startDateMillis = _startDateMillis.asStateFlow()

    private val _endDateString = MutableStateFlow("")
    val endDateString = _endDateString.asStateFlow()

    private val _endDateMillis = MutableStateFlow<Long?>(null)
    val endDateMillis = _endDateMillis.asStateFlow()

    private val _vipFilter = MutableStateFlow("Todos")
    val vipFilter: StateFlow<String> = _vipFilter.asStateFlow()

    private val _stateFilter = MutableStateFlow("Todos")
    val stateFilter: StateFlow<String> = _stateFilter.asStateFlow()

    private val _showConfirmationPopup = MutableStateFlow(false)
    val showConfirmationPopup: StateFlow<Boolean> = _showConfirmationPopup.asStateFlow()

    fun onStartDateSelected(millis: Long) {
      _startDateMillis.value = millis
      _startDateString.value = convertMillisToDateString(millis)

      // --- LÓGICA DE VALIDAÇÃO BÔNUS ---
      // Se a nova data de INÍCIO for DEPOIS da data de FIM já selecionada,
      // nós limpamos a data de FIM para forçar o usuário a escolher de novo.
      _endDateMillis.value?.let { endMillis ->
        if (millis > endMillis) {
          _endDateMillis.value = null
          _endDateString.value = ""
        }
      }
    }

    fun onEndDateSelected(millis: Long) {
      // A validação no DatePicker já garante que 'millis'
      // será >= à data de início, então podemos apenas setar.
      _endDateMillis.value = millis
      _endDateString.value = convertMillisToDateString(millis)
    }

    fun updateVipFilter(filter: String) {
        _vipFilter.value = filter
        customerViewModel.updateVipFilter(filter)
    }

    fun updateStateFilter(filter: String) {
        _stateFilter.value = filter
        customerViewModel.updateStateFilter(filter)
    }

    fun sendCampaign(name: String, description: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            val campaign = Campaign(name, description, startDate, endDate)
            campaignRepository.addCampaign(campaign)
            val filteredCustomers = customerViewModel.filteredCustomers.first()
            _showConfirmationPopup.value = true
        }
    }

    fun dismissConfirmationPopup() {
        _showConfirmationPopup.value = false
    }

    fun convertMillisToDateString(millis: Long): String {
      val instant = Instant.ofEpochMilli(millis)
      val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .withZone(ZoneId.of("UTC"))
      return formatter.format(instant)
    }
}