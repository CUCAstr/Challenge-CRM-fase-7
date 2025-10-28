package br.com.savedra.challengecrm.ui.view

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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.theme.white
import br.com.savedra.challengecrm.viewmodel.CampaignViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCampaignModal(
  onDismiss: () -> Unit,
  viewModel: CampaignViewModel
) {
  val title by viewModel.newCampaignTitle.collectAsState()
  val description by viewModel.newCampaignDescription.collectAsState()
  val startDate by viewModel.newCampaignStartDate.collectAsState()
  val endDate by viewModel.newCampaignEndDate.collectAsState()

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
            onValueChange = { viewModel.onNewCampaignTitleChange(it) },
            label = { Text("Título da campanha") },
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
            onValueChange = { viewModel.onNewCampaignDescriptionChange(it) },
            label = { Text("Descrição da campanha") },
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
            label = "Data inicial da campanha",
            dateString = startDate,
            onDateSelected = { millis ->
              val selectedDate = convertMillisToDateString(millis)
              viewModel.onNewCampaignStartDateChange(selectedDate)
            },
            dateValidator = { utcTimeMillis ->
              utcTimeMillis >= System.currentTimeMillis()
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          DatePickerField(
            label = "Data final da campanha",
            dateString = endDate,
            onDateSelected = { millis ->
              val selectedDate = convertMillisToDateString(millis)
              viewModel.onNewCampaignEndDateChange(selectedDate)
            },
            dateValidator = { utcTimeMillis ->
              utcTimeMillis >= System.currentTimeMillis()
            },
            modifier = Modifier.fillMaxWidth()
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
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              viewModel.sendCampaign()
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
