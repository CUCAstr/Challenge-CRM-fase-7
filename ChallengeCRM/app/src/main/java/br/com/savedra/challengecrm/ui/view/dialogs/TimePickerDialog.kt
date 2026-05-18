package br.com.savedra.challengecrm.ui.view.dialogs

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.*

/**
 * Componente de Diálogo de Hora Renomeado para evitar conflito.
 */
@Composable
fun StandardTimePicker(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
    val initialMinute = calendar.get(Calendar.MINUTE)

    val timeSetListener =
      TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        onTimeSelected(formattedTime)
        onDismiss()
      }

    DisposableEffect(Unit) {
        val picker = TimePickerDialog(
            context,
            timeSetListener,
            initialHour,
            initialMinute,
            true
        ).apply {
            setOnDismissListener { onDismiss() }
            show()
        }
        onDispose { picker.dismiss() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
  label: String,
  timeString: String,
  onTimeSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showDialog by remember { mutableStateOf(false) }

  Box(modifier = modifier) {
    OutlinedTextField(
      value = timeString,
      onValueChange = { },
      modifier = Modifier.fillMaxWidth(),
      readOnly = true,
      label = { Text(label) },
      trailingIcon = {
        IconButton(onClick = { showDialog = true }) {
          Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Abrir seletor de tempo"
          )
        }
      }
    )
  }

  if (showDialog) {
    StandardTimePicker(
        onTimeSelected = onTimeSelected,
        onDismiss = { showDialog = false }
    )
  }
}
