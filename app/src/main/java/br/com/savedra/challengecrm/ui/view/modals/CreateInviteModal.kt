import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.view.dialogs.FilteredClientsDialog
import br.com.savedra.challengecrm.ui.view.dialogs.DatePickerField
import br.com.savedra.challengecrm.ui.view.dialogs.convertMillisToDateString
import br.com.savedra.challengecrm.ui.view.dialogs.TimePickerField
import br.com.savedra.challengecrm.ui.theme.white
import br.com.savedra.challengecrm.viewmodel.InviteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInviteModal(
  onDismiss: () -> Unit,
  viewModel: InviteViewModel
) {
  val title by viewModel.newInviteTitle.collectAsState()
  val description by viewModel.newInviteDescription.collectAsState()
  val date by viewModel.newInviteDate.collectAsState()
  val time by viewModel.newInviteTime.collectAsState()
  val location by viewModel.newInviteLocation.collectAsState()

  val segments = listOf(
    "Todos",
    "ED",
    "IT",
    "Retail & Financial",
    "GRC",
    "HR",
    "Smart Spends",
    "Health",
    "CSC",
    "Field Marketing",
    "Finance",
    "ESG",
    "CX"
  )
  val status = listOf("Todos", "Ativo", "Em negociação", "Inativo", "Aguardando resposta")

  var expandedSegment by remember { mutableStateOf(false) }
  var expandedStatus by remember { mutableStateOf(false) }

  var showFilteredClients by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier.fillMaxSize(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = white)
    ) {
      LazyColumn(
        modifier = Modifier.padding(16.dp)
      ) {
        item {
          OutlinedTextField(
            value = title,
            onValueChange = { viewModel.onNewInviteTitleChange(it) },
            label = { Text("Título do convite") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Unspecified,
              autoCorrectEnabled = true,
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Unspecified
            ),
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = description,
            onValueChange = { viewModel.onNewInviteDescriptionChange(it) },
            label = { Text("Descrição do convite") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Unspecified,
              autoCorrectEnabled = true,
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Unspecified
            ),
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          DatePickerField(
            label = "Data do convite",
            dateString = date,
            onDateSelected = { millis ->
              val selectedDate = convertMillisToDateString(millis)
              viewModel.onNewInviteDateChange(selectedDate)
            },
            dateValidator = { utcTimeMillis ->
              utcTimeMillis >= System.currentTimeMillis()
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          TimePickerField(
            label = "Hora do convite",
            timeString = time,
            onTimeSelected = { newTime ->
              viewModel.onNewInviteTimeChange(newTime)
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = location,
            onValueChange = { viewModel.onNewInviteLocationChange(it) },
            label = { Text("Local do convite") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Unspecified,
              autoCorrectEnabled = true,
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Unspecified
            ),
          )
        }
        item {
          Spacer(modifier = Modifier.height(16.dp))
          Divider()
          Spacer(modifier = Modifier.height(16.dp))
          Text("Filtros de Envio", style = MaterialTheme.typography.titleMedium)
          Spacer(modifier = Modifier.height(16.dp))
        }

        // Filters
        item {
          ExposedDropdownMenuBox(
            expanded = expandedSegment,
            onExpandedChange = { expandedSegment = !expandedSegment },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = viewModel.segmentFilter.collectAsState().value,
              onValueChange = { },
              label = { Text("Segmento") },
              readOnly = true,
              trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment)
              },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            )
            ExposedDropdownMenu(
              expanded = expandedSegment,
              onDismissRequest = { expandedSegment = false },
              modifier = Modifier.fillMaxWidth()
            ) {
              segments.forEach { segment ->
                DropdownMenuItem(
                  text = { Text(segment) },
                  onClick = {
                    viewModel.onSegmentFilterChange(segment)
                    expandedSegment = false
                  }
                )
              }
            }
          }
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          ExposedDropdownMenuBox(
            expanded = expandedStatus,
            onExpandedChange = { expandedStatus = !expandedStatus },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = viewModel.statusFilter.collectAsState().value,
              onValueChange = { },
              label = { Text("Status") },
              readOnly = true,
              trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
              },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            )
            ExposedDropdownMenu(
              expanded = expandedStatus,
              onDismissRequest = { expandedStatus = false },
              modifier = Modifier.fillMaxWidth()
            ) {
              status.forEach { status ->
                DropdownMenuItem(
                  text = { Text(status) },
                  onClick = {
                    viewModel.onStatusFilterChange(status)
                    expandedStatus = false
                  }
                )
              }
            }
          }
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = viewModel.scoreStartFilter.collectAsState().value,
              onValueChange = { viewModel.onScoreStartFilterChange(it) },
              label = { Text("Score Mínimo") },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
              value = viewModel.scoreEndFilter.collectAsState().value,
              onValueChange = { viewModel.onScoreEndFilterChange(it) },
              label = { Text("Score Máximo") },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
          TextButton(onClick = {
            viewModel.getFilteredClients()
            showFilteredClients = true
          }) {
            Text("Exibir resultados dos filtros")
          }
        }

        item {
          if (viewModel.showError.collectAsState().value) {
            Text(
              "Todos os campos devem ser preenchidos.",
              color = MaterialTheme.colorScheme.error,
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(top = 8.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              viewModel.sendInvite() 
              onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Enviar")
          }
        }
      }
    }
  }

  if (showFilteredClients) {
    FilteredClientsDialog(
      clients = viewModel.filteredClients.collectAsState().value,
      onDismiss = { showFilteredClients = false }
    )
  }
}