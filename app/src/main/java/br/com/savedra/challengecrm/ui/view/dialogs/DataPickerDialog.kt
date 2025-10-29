package br.com.savedra.challengecrm.ui.view.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertMillisToDateString(millis: Long): String {
  val instant = Instant.ofEpochMilli(millis)
  val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    .withZone(ZoneId.of("UTC"))
  return formatter.format(instant)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
  label: String,
  dateString: String,
  onDateSelected: (Long) -> Unit,
  dateValidator: (Long) -> Boolean,
  modifier: Modifier = Modifier
) {
  var showDialog by remember { mutableStateOf(false) }
  val datePickerState = rememberDatePickerState(
    selectableDates = object : SelectableDates {
      override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return dateValidator(utcTimeMillis)
      }
    }
  )

  Box(modifier = modifier) {
    OutlinedTextField(
      value = dateString,
      onValueChange = { },
      modifier = Modifier.fillMaxWidth(),
      readOnly = true,
      label = { Text(label) },
      trailingIcon = {
        Icon(
          imageVector = Icons.Default.DateRange,
          contentDescription = "Abrir calendário"
        )
      }
    )
    Box(
      modifier = Modifier
        .matchParentSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = { showDialog = true }
        )
    )
  }

  if (showDialog) {
    DatePickerDialog(
      onDismissRequest = { showDialog = false },
      confirmButton = {
        TextButton(
          onClick = {
            datePickerState.selectedDateMillis?.let { millis ->
              onDateSelected(millis)
            }
            showDialog = false
          },
          enabled = datePickerState.selectedDateMillis != null
        ) {
          Text("OK")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDialog = false }) {
          Text("Cancelar")
        }
      }
    ) {
      DatePicker(
        state = datePickerState
      )
    }
  }
}