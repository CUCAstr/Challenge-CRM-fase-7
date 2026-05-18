package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.EventsCenter
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.dialogs.DatePickerField
import br.com.savedra.challengecrm.ui.view.dialogs.convertMillisToDateString
import br.com.savedra.challengecrm.viewmodel.EventViewModel
import java.util.Calendar

/**
 * Tela de Solicitação de Orçamento para Eventos.
 */
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

  val eventUiState by eventViewModel.eventUiState.collectAsState()
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(eventUiState) {
    if (eventUiState is br.com.savedra.challengecrm.viewmodel.EventUIState.Success) {
      Toast.makeText(context, "Orçamento solicitado com sucesso!", Toast.LENGTH_LONG).show()
      navController.navigate(AppRoutes.CLIENT_HOME) { popUpTo(AppRoutes.CLIENT_HOME) { this.inclusive = true } }
      eventViewModel.resetUiState()
    }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
        TopAppBar(
            title = { Text("Eventos", color = slate800, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
        )
    },
    bottomBar = {
        BottomNavigationClient(
            onInboxClick = onInboxClick,
            onLogoutClick = onLogoutClick,
            isInboxActive = false,
            isEventsCenterActive = true,
            isBusinessClubActive = false,
            isSheratonHotelActive = false,
            onEventsCenterClick = { },
            onBusinessClubClick = onBusinessClubClick,
            onSheratonHotelClick = onSheratonHotelClick
        )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(24.dp)
    ) {
      Text(
        text = "REALIZE SEU EVENTO",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = slate800,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = eventName,
        onValueChange = { eventName = it },
        label = { Text("Nome do evento *", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(16.dp))

      DatePickerField(
        label = "Data do evento *",
        dateString = eventDate,
        onDateSelected = { eventDate = convertMillisToDateString(it) },
        dateValidator = { true }, // Adicionado parâmetro faltante
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = numberOfPeople,
        onValueChange = { if (it.all { c -> c.isDigit() }) numberOfPeople = it },
        label = { Text("Nº de pessoas *", color = slate700) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = numberOfApartments,
        onValueChange = { if (it.all { c -> c.isDigit() }) numberOfApartments = it },
        label = { Text("Nº de apartamentos", color = slate700) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
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
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = slate800)
      ) {
        if (eventUiState is br.com.savedra.challengecrm.viewmodel.EventUIState.Loading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = white)
        } else {
            Text("SOLICITAR ORÇAMENTO", fontWeight = FontWeight.Bold)
        }
      }
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
