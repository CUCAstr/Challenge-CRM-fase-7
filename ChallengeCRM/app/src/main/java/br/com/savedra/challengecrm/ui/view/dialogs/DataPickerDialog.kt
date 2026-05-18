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
import androidx.compose.material3.IconButton
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
import java.time.LocalDate

fun convertMillisToDateString(millis: Long): String {
  val instant = Instant.ofEpochMilli(millis)
  val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    .withZone(ZoneId.of("UTC"))
  return formatter.format(instant)
}

/**
 * Componente de Diálogo de Data Renomeado para evitar conflito com Material3.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardDatePicker(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    dateValidator: (Long) -> Boolean = { true }
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return dateValidator(utcTimeMillis)
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    onDismiss()
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
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

  Box(modifier = modifier) {
    OutlinedTextField(
      value = dateString,
      onValueChange = { },
      modifier = Modifier.fillMaxWidth(),
      readOnly = true,
      label = { Text(label) },
      trailingIcon = {
        IconButton(onClick = { showDialog = true }) {
          Icon(imageVector = Icons.Default.DateRange, contentDescription = "Abrir calendário")
        }
      }
    )
  }

  if (showDialog) {
    StandardDatePicker(
        onDateSelected = onDateSelected,
        onDismiss = { showDialog = false },
        dateValidator = dateValidator
    )
  }
}
