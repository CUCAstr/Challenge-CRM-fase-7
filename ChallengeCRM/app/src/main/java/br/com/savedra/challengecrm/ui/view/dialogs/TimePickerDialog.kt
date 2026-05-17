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
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
  label: String,
  timeString: String,
  onTimeSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showDialog by remember { mutableStateOf(false) }
  val context = LocalContext.current
  val calendar = Calendar.getInstance()

  val initialHour = if (timeString.isNotBlank()) {
    timeString.split(":")[0].toInt()
  } else {
    calendar.get(Calendar.HOUR_OF_DAY)
  }

  val initialMinute = if (timeString.isNotBlank()) {
    timeString.split(":")[1].toInt()
  } else {
    calendar.get(Calendar.MINUTE)
  }

  Box(modifier = modifier) {
    OutlinedTextField(
      value = timeString,
      onValueChange = { },
      modifier = Modifier.fillMaxWidth(),
      readOnly = true,
      label = { Text(label) },
      trailingIcon = {
        IconButton(onClick = { showDialog = true }) { // CORREÇÃO: Gatilho apenas no ícone
          Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "Abrir seletor de tempo"
          )
        }
      }
    )
    // REMOVIDO: O Box que cobria todo o campo.
  }

  if (showDialog) {
    val timeSetListener =
      TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
        onTimeSelected(String.format("%02d:%02d", hour, minute))
        showDialog = false
      }

    TimePickerDialog(
      context,
      timeSetListener,
      initialHour,
      initialMinute,
      true
    ).apply {
      setOnDismissListener { showDialog = false }
      show()
    }
  }
}
