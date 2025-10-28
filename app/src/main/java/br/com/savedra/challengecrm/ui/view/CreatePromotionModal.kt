package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.view.DatePickerField
import br.com.savedra.challengecrm.ui.view.TimePickerField
import br.com.savedra.challengecrm.ui.view.convertMillisToDateString
import br.com.savedra.challengecrm.ui.theme.white
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
  var expandedSegment by remember { mutableStateOf(false) }

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
            onValueChange = { viewModel.onNewPromotionTitleChange(it) },
            label = { Text("Título da promoção") },
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
            onValueChange = { viewModel.onNewPromotionDescriptionChange(it) },
            label = { Text("Descrição da promoção") },
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
            value = originalValue,
            onValueChange = { viewModel.onNewPromotionOriginalValueChange(it) },
            label = { Text("Valor original") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Unspecified,
              autoCorrectEnabled = true,
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Unspecified
            ),
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = promotionValue,
            onValueChange = { viewModel.onNewPromotionPromotionValueChange(it) },
            label = { Text("Valor da promoção") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
              capitalization = KeyboardCapitalization.Unspecified,
              autoCorrectEnabled = true,
              keyboardType = KeyboardType.Number,
              imeAction = ImeAction.Unspecified
            ),
          )
        }
        item {
          Spacer(modifier = Modifier.height(8.dp))
          DatePickerField(
            label = "Data de expiração",
            dateString = dateExpiresIn,
            onDateSelected = { millis ->
              val selectedDate = convertMillisToDateString(millis)
              viewModel.onNewPromotionDateExpiresInChange(selectedDate)
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
            label = "Hora de expiração",
            timeString = hoursExpiresIn,
            onTimeSelected = { newTime ->
              viewModel.onNewPromotionHoursExpiresInChange(newTime)
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
        item {
          Spacer(modifier = Modifier.height(16.dp))
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
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = {
              viewModel.sendPromotion()
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
}
