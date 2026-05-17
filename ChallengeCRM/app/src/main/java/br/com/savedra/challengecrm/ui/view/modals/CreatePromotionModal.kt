package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.view.dialogs.FilteredClientsDialog
import br.com.savedra.challengecrm.ui.view.dialogs.DatePickerField
import br.com.savedra.challengecrm.ui.view.dialogs.TimePickerField
import br.com.savedra.challengecrm.ui.view.dialogs.convertMillisToDateString
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.PromotionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePromotionModal(
  onDismiss: () -> Unit,
  viewModel: PromotionViewModel
) {
  val title by viewModel.newPromotionTitle.collectAsState()
  val description by viewModel.newPromotionDescription.collectAsState()
  val originalValue by viewModel.newPromotionOriginalValue.collectAsState()
  val promotionValue by viewModel.newPromotionPromotionValue.collectAsState()
  val dateExpiresIn by viewModel.newPromotionDateExpiresIn.collectAsState()
  val hoursExpiresIn by viewModel.newPromotionHoursExpiresIn.collectAsState()

  val segmentFilter by viewModel.segmentFilter.collectAsState()
  val statusFilter by viewModel.statusFilter.collectAsState()
  val scoreStartFilter by viewModel.scoreStartFilter.collectAsState()
  val scoreEndFilter by viewModel.scoreEndFilter.collectAsState()
  val filteredClients by viewModel.filteredClients.collectAsState()
  val showError by viewModel.showError.collectAsState()

  val segments = listOf("Todos", "ED", "IT", "Finance", "ESG", "CX")
  val statusList = listOf("Todos", "Ativo", "Inativo")

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
      LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
          Text("Nova Promoção", style = MaterialTheme.typography.headlineSmall, color = slate800, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))
        }
        item {
          OutlinedTextField(
            value = title,
            onValueChange = { viewModel.onNewPromotionTitleChange(it) },
            label = { Text("Título da promoção") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedContainerColor = white, unfocusedContainerColor = white
            )
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = description,
            onValueChange = { viewModel.onNewPromotionDescriptionChange(it) },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedContainerColor = white, unfocusedContainerColor = white
            )
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = originalValue,
              onValueChange = { viewModel.onNewPromotionOriginalValueChange(it) },
              label = { Text("Valor Original") },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
              value = promotionValue,
              onValueChange = { viewModel.onNewPromotionPromotionValueChange(it) },
              label = { Text("Valor Promocional") },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
          }
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          DatePickerField(
            label = "Data de expiração",
            dateString = dateExpiresIn,
            onDateSelected = { millis -> viewModel.onNewPromotionDateExpiresInChange(convertMillisToDateString(millis)) },
            dateValidator = { utcTimeMillis -> utcTimeMillis >= System.currentTimeMillis() },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          TimePickerField(
            label = "Hora de expiração",
            timeString = hoursExpiresIn,
            onTimeSelected = { viewModel.onNewPromotionHoursExpiresInChange(it) },
            modifier = Modifier.fillMaxWidth()
          )
        }
        
        item {
          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(16.dp))
          Text("Público Alvo", style = MaterialTheme.typography.titleMedium, color = slate800)
        }

        item {
          Spacer(modifier = Modifier.height(8.dp))
          ExposedDropdownMenuBox(
            expanded = expandedSegment,
            onExpandedChange = { expandedSegment = !expandedSegment }
          ) {
            OutlinedTextField(
              value = segmentFilter,
              onValueChange = { },
              label = { Text("Segmento") },
              readOnly = true,
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment) },
              modifier = Modifier.menuAnchor().fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
            ExposedDropdownMenu(expanded = expandedSegment, onDismissRequest = { expandedSegment = false }, modifier = Modifier.background(white)) {
              segments.forEach { s ->
                DropdownMenuItem(text = { Text(s, color = Color.Black) }, onClick = { viewModel.onSegmentFilterChange(s); expandedSegment = false })
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
          if (showError) {
              Text("Preencha todos os campos.", color = Color.Red, fontSize = 12.sp)
          }
          Button(
            onClick = { viewModel.sendPromotion(); onDismiss() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
          ) {
            Text("Criar Promoção")
          }
          Spacer(modifier = Modifier.height(8.dp))
          TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar", color = slate600)
          }
        }
      }
    }
  }
}
