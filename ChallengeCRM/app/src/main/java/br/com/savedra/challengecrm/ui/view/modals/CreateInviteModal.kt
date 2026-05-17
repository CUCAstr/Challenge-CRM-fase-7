package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.InviteViewModel

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText

/**
 * Máscara para Data (DD/MM/AAAA)
 */
class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = StringBuilder()
        for (i in text.indices) {
            out.append(text[i])
            if (i == 1 || i == 3) out.append("/")
        }
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                return offset + 2
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return offset - 2
            }
        }
        return TransformedText(AnnotatedString(out.toString()), numberOffsetTranslator)
    }
}

/**
 * Máscara para Hora (HH:MM)
 */
class TimeTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = StringBuilder()
        for (i in text.indices) {
            out.append(text[i])
            if (i == 1) out.append(":")
        }
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                return offset + 1
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset > 2) return offset - 1
                return offset
            }
        }
        return TransformedText(AnnotatedString(out.toString()), numberOffsetTranslator)
    }
}

/**
 * Modal para criação de novos convites.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInviteModal(
  onDismiss: () -> Unit,
  viewModel: InviteViewModel
) {
  val newInviteTitle by viewModel.newInviteTitle.collectAsState()
  val newInviteDescription by viewModel.newInviteDescription.collectAsState()
  val newInviteDate by viewModel.newInviteDate.collectAsState()
  val newInviteLocation by viewModel.newInviteLocation.collectAsState()
  val newInviteTime by viewModel.newInviteTime.collectAsState()

  val segmentFilter by viewModel.segmentFilter.collectAsState()
  val showError by viewModel.showError.collectAsState()

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = slate100)
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth()
      ) {
        Text(
          text = "Novo Convite",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            OutlinedTextField(
              value = newInviteTitle,
              onValueChange = { viewModel.onNewInviteTitleChange(it) },
              label = { Text("Nome do Evento") },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
              )
            )
          }
          item {
            OutlinedTextField(
              value = newInviteDescription,
              onValueChange = { viewModel.onNewInviteDescriptionChange(it) },
              label = { Text("Descrição Detalhada") },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
              )
            )
          }
          item {
            OutlinedTextField(
              value = newInviteDate,
              onValueChange = { if (it.length <= 8) viewModel.onNewInviteDateChange(it) },
              label = { Text("Data do Evento (DDMMYYYY)") },
              visualTransformation = DateTransformation(),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
              )
            )
          }
          item {
            OutlinedTextField(
              value = newInviteTime,
              onValueChange = { if (it.length <= 4) viewModel.onNewInviteTimeChange(it) },
              label = { Text("Hora do Evento (HHMM)") },
              visualTransformation = TimeTransformation(),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
              )
            )
          }
          item {
            OutlinedTextField(
              value = newInviteLocation,
              onValueChange = { viewModel.onNewInviteLocationChange(it) },
              label = { Text("Localização") },
              modifier = Modifier.fillMaxWidth(),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
              )
            )
          }
          item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = slate200)
            Text(text = "Segmento Alvo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = slate700)
            
            var expanded by remember { mutableStateOf(false) }
            val segments = listOf("Todos", "ED", "IT", "Finance", "ESG", "CX")

            ExposedDropdownMenuBox(
              expanded = expanded,
              onExpandedChange = { expanded = !expanded }
            ) {
              OutlinedTextField(
                value = segmentFilter,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                )
              )
              ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                segments.forEach { seg ->
                  DropdownMenuItem(text = { Text(seg, color = Color.Black) }, onClick = {
                    viewModel.onSegmentFilterChange(seg)
                    expanded = false
                  })
                }
              }
            }
          }
        }

        if (showError) {
          Text(text = "Preencha todos os campos.", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = onDismiss) { Text("Cancelar", color = slate600) }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = { viewModel.sendInvite(); onDismiss() },
            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
          ) {
            Text("Enviar")
          }
        }
      }
    }
  }
}
