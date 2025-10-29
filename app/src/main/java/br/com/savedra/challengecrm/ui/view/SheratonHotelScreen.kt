package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.BusinessClub
import br.com.savedra.challengecrm.model.SheratonHotel
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.viewmodel.BusinessClubViewModel
import br.com.savedra.challengecrm.viewmodel.SheratonHotelViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheratonHotelScreen(
    navController: NavController,
    onLogoutClick: () -> Unit,
    onInboxClick: () -> Unit,
    onEventsCenterClick: () -> Unit,
    onBusinessClubClick: () -> Unit,
    sheratonHotelViewModel: SheratonHotelViewModel = viewModel()
) {
    var checkInDateMillis by remember { mutableStateOf("") }
    var checkOutDateMillis by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf("Standard") }
    var specialRequests by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val sheratonHotelUiState by sheratonHotelViewModel.sheratonUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(sheratonHotelUiState) {
        when (sheratonHotelUiState) {
            is br.com.savedra.challengecrm.viewmodel.SheratonHotelUIState.Success -> {
                Toast.makeText(context, "Pedido de reserva enviada com sucesso!", Toast.LENGTH_LONG).show()
                navController.navigate(AppRoutes.CLIENT_HOME) {
                    popUpTo(AppRoutes.CLIENT_HOME) { inclusive = true }
                }
                sheratonHotelViewModel.resetUiState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(slate50)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sheraton Hotel Reservation",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DatePickerField(
                label = "Check-in",
                dateString = checkInDateMillis,
                onDateSelected = { checkInDateMillis = convertMillisToDateString(it) },
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerField(
                label = "Check-out",
                dateString = checkOutDateMillis,
                onDateSelected = { checkOutDateMillis = convertMillisToDateString(it) },
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
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = guests,
                onValueChange = { guests = it },
                label = { Text("Hóspedes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            RoomTypeSelector(
                selectedType = roomType,
                onTypeSelected = { roomType = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = specialRequests,
                onValueChange = { specialRequests = it },
                label = { Text("Solicitações especiais") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val event = SheratonHotel(
                        checkInDate = checkInDateMillis,
                        checkOutDate = checkOutDateMillis,
                        guests = guests,
                        roomType = roomType,
                        specialRequests = specialRequests
                    )
                    sheratonHotelViewModel.saveSheratonHotel(event)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("PEDIR RESERVA")
            }

            when (sheratonHotelUiState) {
                is br.com.savedra.challengecrm.viewmodel.SheratonHotelUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is br.com.savedra.challengecrm.viewmodel.SheratonHotelUIState.Error -> {
                    val errorMessage = (sheratonHotelUiState as br.com.savedra.challengecrm.viewmodel.EventUIState.Error).message
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
            isEventsCenterActive = false,
            isBusinessClubActive = false,
            isSheratonHotelActive = true,
            modifier = Modifier.align(Alignment.BottomCenter),
            onEventsCenterClick = onEventsCenterClick,
            onBusinessClubClick = onBusinessClubClick,
            onSheratonHotelClick = { }
        )
    }
}

@Composable
fun RoomTypeSelector(selectedType: String, onTypeSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Tipo de quarto:", modifier = Modifier.weight(1f))
        Row(modifier = Modifier.weight(2f)) {
            RadioButton(
                selected = selectedType == "Standard",
                onClick = { onTypeSelected("Standard") }
            )
            Text("Standard", modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedType == "Deluxe",
                onClick = { onTypeSelected("Deluxe") }
            )
            Text("Deluxe", modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}