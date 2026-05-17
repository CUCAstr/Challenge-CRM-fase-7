package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.EventsCenter
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.ui.theme.slate600
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.ui.view.dialogs.DatePickerField
import br.com.savedra.challengecrm.ui.view.dialogs.convertMillisToDateString
import br.com.savedra.challengecrm.viewmodel.EventViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsCenterScreen(
  navController: NavController,
  onLogoutClick: () -> Unit,
  onInboxClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  eventViewModel: EventViewModel = viewModel()
) {
  var eventName by remember { mutableStateOf("") }
  var eventDate by remember { mutableStateOf("") }
  var segment by remember { mutableStateOf("") }
  var numberOfPeople by remember { mutableStateOf("") }
  var eventSetup by remember { mutableStateOf("") }
  var audienceProfile by remember { mutableStateOf("") }
  val knowHotel = remember { mutableStateOf<Boolean?>(null) }
  val firstTimeEvent = remember { mutableStateOf<Boolean?>(null) }
  var eventPeriodicity by remember { mutableStateOf("") }
  var competingWith by remember { mutableStateOf("") }
  var budget by remember { mutableStateOf("") }
  val dateFlexibility = remember { mutableStateOf<Boolean?>(null) }
  val accommodationDemand = remember { mutableStateOf<Boolean?>(null) }
  var numberOfApartments by remember { mutableStateOf("") }
  var decisiveFactor by remember { mutableStateOf("") }
  var necessaryDifferential by remember { mutableStateOf("") }
  val internalPartnersContact = remember { mutableStateOf<Boolean?>(null) }

  var eventSetupExpanded by remember { mutableStateOf(false) }
  var segmentExpanded by remember { mutableStateOf(false) }

  val eventUiState by eventViewModel.eventUiState.collectAsState()
  val context = LocalContext.current

  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
  }

  LaunchedEffect(eventUiState) {
    when (eventUiState) {
      is br.com.savedra.challengecrm.viewmodel.EventUIState.Success -> {
        Toast.makeText(context, "Orçamento solicitado com sucesso!", Toast.LENGTH_LONG).show()
        navController.navigate(AppRoutes.CLIENT_HOME) {
          popUpTo(AppRoutes.CLIENT_HOME) { this.inclusive = true }
        }
        eventViewModel.resetUiState()
      }

      else -> {}
    }
  }

  var eventSetupOptions = listOf(
    "Formato auditório", "Formato escolar",
    "Formato U", "Formato meia lua", "Formato coquetel", "Formato buffet",
    "Formato empratado", "Formato coffee", "Outro formato"
  )

  val segmentOptions = listOf(
    "ED", "IT", "Retail & Financial",
    "GRC", "HR", "Smart Spends", "Health", "CSC", "Field Marketing",
    "Finance", "ESG", "CX"
  )

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(slate50)
  ) {
    Column(
      modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(24.dp)
    ) {
      Text(
        text = "REALIZE SEU EVENTO",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = slate800,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Utilize o formulário e solicite seu orçamento, nossa equipe está preparada para tornar do seu projeto um grande sucesso.",
        fontSize = 16.sp,
        color = slate600,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = eventName,
        onValueChange = { eventName = it },
        label = { Text("Nome do evento *") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      DatePickerField(
        label = "Data do evento *",
        dateString = eventDate,
        onDateSelected = { eventDate = convertMillisToDateString(it) },
        dateValidator = { date ->
          val today = Calendar.getInstance()
          today.set(Calendar.HOUR_OF_DAY, 0)
          today.set(Calendar.MINUTE, 0)
          today.set(Calendar.SECOND, 0)
          today.set(Calendar.MILLISECOND, 0)
          val sevenDaysFromNow = Calendar.getInstance()
          sevenDaysFromNow.add(Calendar.DAY_OF_YEAR, 7)
          date >= today.timeInMillis && date <= sevenDaysFromNow.timeInMillis
        },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      ExposedDropdownMenuBox(
        expanded = segmentExpanded,
        onExpandedChange = { segmentExpanded = !segmentExpanded }
      ) {
        OutlinedTextField(
          value = segment,
          onValueChange = {},
          label = { Text("Segmento de atuação *") },
          readOnly = true,
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = segmentExpanded)
          },
          modifier = Modifier
              .fillMaxWidth()
              .menuAnchor()
        )
        ExposedDropdownMenu(
          expanded = segmentExpanded,
          onDismissRequest = { segmentExpanded = false }
        ) {
          segmentOptions.forEach { option ->
            DropdownMenuItem(
              text = { Text(option) },
              onClick = {
                segment = option
                segmentExpanded = false
              }
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = numberOfPeople,
        onValueChange = { numberOfPeople = it },
        label = { Text("Para quantas pessoas você planeja o seu evento? *") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      ExposedDropdownMenuBox(
        expanded = eventSetupExpanded,
        onExpandedChange = { eventSetupExpanded = !eventSetupExpanded }
      ) {
        OutlinedTextField(
          value = eventSetup,
          onValueChange = {},
          label = { Text("Qual montagem planeja para seu evento? *") },
          readOnly = true,
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventSetupExpanded)
          },
          modifier = Modifier
              .fillMaxWidth()
              .menuAnchor()
        )
        ExposedDropdownMenu(
          expanded = eventSetupExpanded,
          onDismissRequest = { eventSetupExpanded = false }
        ) {
          eventSetupOptions.forEach { option ->
            DropdownMenuItem(
              text = { Text(option) },
              onClick = {
                eventSetup = option
                eventSetupExpanded = false
              }
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = audienceProfile,
        onValueChange = { audienceProfile = it },
        label = { Text("Perfil do público (faixa etária, segmento profissional, etc.)") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      Text("Já conhece o hotel? *", fontSize = 14.sp)
      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = knowHotel.value == true,
          onClick = { knowHotel.value = true }
        )
        Text("Sim")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
          selected = knowHotel.value == false,
          onClick = { knowHotel.value = false }
        )
        Text("Não")
      }
      Spacer(modifier = Modifier.height(16.dp))

      Text("É a primeira vez que o evento ocorre? *", fontSize = 14.sp)
      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = firstTimeEvent.value == true,
          onClick = { firstTimeEvent.value = true }
        )
        Text("Sim")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
          selected = firstTimeEvent.value == false,
          onClick = { firstTimeEvent.value = false }
        )
        Text("Não")
      }
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = eventPeriodicity,
        onValueChange = { eventPeriodicity = it },
        label = { Text("Se não, qual é a periodicidade? E onde aconteceu antes?") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = competingWith,
        onValueChange = { competingWith = it },
        label = { Text("Estamos concorrendo com algum hotel ou espaço de eventos?") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = budget,
        onValueChange = { budget = it },
        label = { Text("O evento tem budget definido? Se sim, qual a estimativa?") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      Text("Flexibilidade para data? *", fontSize = 14.sp)
      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = dateFlexibility.value == true,
          onClick = { dateFlexibility.value = true }
        )
        Text("Sim")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
          selected = dateFlexibility.value == false,
          onClick = { dateFlexibility.value = false }
        )
        Text("Não")
      }
      Spacer(modifier = Modifier.height(16.dp))

      Text("O evento terá demanda de hospedagem? *", fontSize = 14.sp)
      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = accommodationDemand.value == true,
          onClick = { accommodationDemand.value = true }
        )
        Text("Sim")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
          selected = accommodationDemand.value == false,
          onClick = { accommodationDemand.value = false }
        )
        Text("Não")
      }
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = numberOfApartments,
        onValueChange = { numberOfApartments = it },
        label = { Text("Se sim, quantos apartamentos precisaria?") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = decisiveFactor,
        onValueChange = { decisiveFactor = it },
        label = { Text("Qual seria o fator decisivo para a escolha do local do evento?") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = necessaryDifferential,
        onValueChange = { necessaryDifferential = it },
        label = { Text("Existe algum diferencial necessário a ser destacado?") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      Text(
        "Informamos que sua solicitação será direcionada aos nossos parceiros internos para serviços adicionais. Caso prefira não receber este contato, por favor nos sinalize. *",
        fontSize = 14.sp
      )
      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = internalPartnersContact.value == true,
          onClick = { internalPartnersContact.value = true }
        )
        Text("Sim")
        Spacer(modifier = Modifier.width(16.dp))
        RadioButton(
          selected = internalPartnersContact.value == false,
          onClick = { internalPartnersContact.value = false }
        )
        Text("Não")
      }
      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          val event = EventsCenter(
            eventName = eventName,
            eventDate = eventDate,
            segment = segment,
            numberOfPeople = numberOfPeople,
            eventSetup = eventSetup,
            audienceProfile = audienceProfile,
            knowHotel = knowHotel.value,
            firstTimeEvent = firstTimeEvent.value,
            eventPeriodicity = eventPeriodicity,
            competingWith = competingWith,
            budget = budget,
            dateFlexibility = dateFlexibility.value,
            accommodationDemand = accommodationDemand.value,
            numberOfApartments = numberOfApartments,
            decisiveFactor = decisiveFactor,
            necessaryDifferential = necessaryDifferential,
            internalPartnersContact = internalPartnersContact.value
          )
          eventViewModel.saveEvent(event)
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("SOLICITAR ORÇAMENTO")
      }

      when (eventUiState) {
        is br.com.savedra.challengecrm.viewmodel.EventUIState.Loading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        is br.com.savedra.challengecrm.viewmodel.EventUIState.Error -> {
          val errorMessage =
            (eventUiState as br.com.savedra.challengecrm.viewmodel.EventUIState.Error).message
          Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        else -> {}
      }

      Spacer(modifier = Modifier.height(80.dp))
    }

    BottomNavigationClient(
      onInboxClick = onInboxClick,
      onLogoutClick = onLogoutClick,
      isInboxActive = false,
      isEventsCenterActive = true,
      isBusinessClubActive = false,
      isSheratonHotelActive = false,
      modifier = Modifier.align(Alignment.BottomCenter),
      onEventsCenterClick = { },
      onBusinessClubClick = onBusinessClubClick,
      onSheratonHotelClick = onSheratonHotelClick
    )
  }
}