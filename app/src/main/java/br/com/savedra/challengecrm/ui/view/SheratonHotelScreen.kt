package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.ui.view.BottomNavigationClient
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SheratonHotelScreen(
    onLogoutClick: () -> Unit,
    onInboxClick: () -> Unit,
    onEventsCenterClick: () -> Unit,
    onBusinessClubClick: () -> Unit
) {
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("") }
    var roomType by remember { mutableStateOf("Standard") }
    var specialRequests by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

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
                text = "Sheraton Hotel",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Peça sua reserva em nosso hotel filiado.",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DatePickerField(
                label = "Check-in",
                dateString = checkInDate,
                onDateSelected = { checkInDate = convertMillisToDateString(it) },
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

            DatePickerField(
                label = "Check-out",
                dateString = checkOutDate,
                onDateSelected = { checkOutDate = convertMillisToDateString(it) },
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
                onClick = { showConfirmationDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pedir reserva")
            }
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

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text("Reserva em análise!") },
                text = { Text("Em breve você será notificado com mais informações.") },
                confirmButton = {
                    Button(onClick = { showConfirmationDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
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
