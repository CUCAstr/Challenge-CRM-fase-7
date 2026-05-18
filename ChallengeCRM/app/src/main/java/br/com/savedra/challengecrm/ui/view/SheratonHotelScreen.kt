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
import br.com.savedra.challengecrm.model.SheratonHotel
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.SheratonHotelUIState
import br.com.savedra.challengecrm.viewmodel.SheratonHotelViewModel

/**
 * Tela de Reserva/Contato para Sheraton Hotel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheratonHotelScreen(
  navController: NavController,
  onLogoutClick: () -> Unit,
  onInboxClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  viewModel: SheratonHotelViewModel = viewModel()
) {
  var checkInDate by remember { mutableStateOf("") }
  var checkOutDate by remember { mutableStateOf("") }
  var guests by remember { mutableStateOf("") }
  var roomType by remember { mutableStateOf("") }

  val uiState by viewModel.sheratonUiState.collectAsState()
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(uiState) {
    if (uiState is SheratonHotelUIState.Success) {
      Toast.makeText(context, "Reserva solicitada com sucesso!", Toast.LENGTH_LONG).show()
      navController.navigate(AppRoutes.CLIENT_HOME) { popUpTo(AppRoutes.CLIENT_HOME) { this.inclusive = true } }
      viewModel.resetUiState()
    }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
        TopAppBar(
            title = { Text("Sheraton Hotel", color = slate800, fontWeight = FontWeight.Bold) },
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
            isEventsCenterActive = false,
            isBusinessClubActive = false,
            isSheratonHotelActive = true,
            onEventsCenterClick = onEventsCenterClick,
            onBusinessClubClick = onBusinessClubClick,
            onSheratonHotelClick = { }
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
        text = "RESERVE SUA ESTADIA",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = slate800,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = checkInDate,
        onValueChange = { checkInDate = it },
        label = { Text("Data de Check-in *", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = checkOutDate,
        onValueChange = { checkOutDate = it },
        label = { Text("Data de Check-out *", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = guests,
        onValueChange = { if (it.all { c -> c.isDigit() }) guests = it },
        label = { Text("Número de Hóspedes *", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          val sh = SheratonHotel(checkInDate = checkInDate, checkOutDate = checkOutDate, guests = guests, roomType = roomType)
          viewModel.saveSheratonHotel(sh)
        },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = slate800)
      ) {
        if (uiState is SheratonHotelUIState.Loading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = white)
        } else {
            Text("SOLICITAR RESERVA", fontWeight = FontWeight.Bold)
        }
      }
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
