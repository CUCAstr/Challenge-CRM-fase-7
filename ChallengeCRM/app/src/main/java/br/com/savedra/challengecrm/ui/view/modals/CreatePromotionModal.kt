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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.view.dialogs.StandardDatePicker
import br.com.savedra.challengecrm.ui.view.dialogs.StandardTimePicker
import br.com.savedra.challengecrm.ui.view.dialogs.convertMillisToDateString
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.PromotionViewModel
import br.com.savedra.challengecrm.ui.view.components.*

/**
 * Modal para criação de novas promoções.
 */
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
  val scoreStartFilter by viewModel.scoreStartFilter.collectAsState()
  val scoreEndFilter by viewModel.scoreEndFilter.collectAsState()
  val showError by viewModel.showError.collectAsState()

  var showDatePicker by remember { mutableStateOf(false) }
  var showTimePicker by remember { mutableStateOf(false) }

  val segments = listOf("Todos", "ED", "IT", "Finance", "ESG", "CX")
  var expandedSegment by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = white)
    ) {
      LazyColumn(modifier = Modifier.padding(24.dp)) {
        item {
          Text("Nova Promoção", style = MaterialTheme.typography.headlineSmall, color = slate800, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
          StandardTextField(
            value = title,
            onValueChange = { viewModel.onNewPromotionTitleChange(it) },
            label = "Título da Promoção"
          )
        }
        
        item {
          Spacer(modifier = Modifier.height(12.dp))
          StandardTextField(
            value = description,
            onValueChange = { viewModel.onNewPromotionDescriptionChange(it) },
            label = "Descrição"
          )
        }

        item {
          Spacer(modifier = Modifier.height(12.dp))
          Row(modifier = Modifier.fillMaxWidth()) {
            StandardTextField(
              value = originalValue,
              onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onNewPromotionOriginalValueChange(it) },
              label = "Valor De",
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StandardTextField(
              value = promotionValue,
              onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onNewPromotionPromotionValueChange(it) },
              label = "Valor Por",
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(12.dp))
          StandardDateInput(
            label = "Data de Expiração (DDMMYYYY)",
            value = dateExpiresIn.replace("/",""),
            onValueChange = { viewModel.onNewPromotionDateExpiresInChange(it) },
            onIconClick = { showDatePicker = true },
            visualTransformation = DateTransformation()
          )
        }

        item {
          Spacer(modifier = Modifier.height(12.dp))
          StandardTimeInput(
            label = "Hora de Expiração (HHMM)",
            value = hoursExpiresIn.replace(":",""),
            onValueChange = { viewModel.onNewPromotionHoursExpiresInChange(it) },
            onIconClick = { showTimePicker = true },
            visualTransformation = TimeTransformation()
          )
        }
        
        item {
          Spacer(modifier = Modifier.height(24.dp))
          HorizontalDivider(color = slate200)
          Spacer(modifier = Modifier.height(16.dp))
          Text("Filtros de Segmentação", style = MaterialTheme.typography.titleMedium, color = slate800, fontWeight = FontWeight.Bold)
        }

        item {
          Spacer(modifier = Modifier.height(12.dp))
          ExposedDropdownMenuBox(
            expanded = expandedSegment,
            onExpandedChange = { expandedSegment = !expandedSegment }
          ) {
            OutlinedTextField(
              value = segmentFilter,
              onValueChange = { },
              label = { Text("Segmento Alvo", color = slate700) },
              readOnly = true,
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment) },
              modifier = Modifier.menuAnchor().fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
            ExposedDropdownMenu(
                expanded = expandedSegment, 
                onDismissRequest = { expandedSegment = false },
                modifier = Modifier.background(white)
            ) {
              segments.forEach { s ->
                DropdownMenuItem(
                    text = { Text(s, color = Color.Black) }, 
                    onClick = { viewModel.onSegmentFilterChange(s); expandedSegment = false },
                    modifier = Modifier.background(white)
                )
              }
            }
          }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
              StandardTextField(
                value = scoreStartFilter,
                onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onScoreStartFilterChange(it) },
                label = "Score Mín",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
              )
              Spacer(modifier = Modifier.width(8.dp))
              StandardTextField(
                value = scoreEndFilter,
                onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onScoreEndFilterChange(it) },
                label = "Score Máx",
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
              )
            }
          }

        item {
          Spacer(modifier = Modifier.height(32.dp))
          if (showError) {
              Text("Por favor, preencha todos os campos obrigatórios.", color = Color.Red, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(8.dp))
          }
          Button(
            onClick = { viewModel.sendPromotion(); onDismiss() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
          ) {
            Text("CRIAR E ENVIAR PROMOÇÃO", color = Color.White, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(12.dp))
          TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("CANCELAR", color = slate600, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
    
    if (showDatePicker) {
        StandardDatePicker(
            onDateSelected = { viewModel.onNewPromotionDateExpiresInChange(convertMillisToDateString(it)); showDatePicker = false },
            onDismiss = { showDatePicker = false }
        )
    }
    if (showTimePicker) {
        StandardTimePicker(
            onTimeSelected = { viewModel.onNewPromotionHoursExpiresInChange(it); showTimePicker = false },
            onDismiss = { showTimePicker = false }
        )
    }
  }
}
