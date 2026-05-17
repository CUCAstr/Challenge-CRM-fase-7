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

/**
 * Modal para criação de novos convites.
 * 
 * CORREÇÃO: Adicionado o cabeçalho 'package' faltante para que o projeto reconheça o arquivo.
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
  val filteredClients by viewModel.filteredClients.collectAsState()

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
              label = { Text("Título") },
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
              label = { Text("Descrição") },
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
              onValueChange = { viewModel.onNewInviteDateChange(it) },
              label = { Text("Data (Ex: 30/10)") },
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
              onValueChange = { viewModel.onNewInviteTimeChange(it) },
              label = { Text("Hora") },
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
              label = { Text("Local") },
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
                  DropdownMenuItem(text = { Text(seg) }, onClick = {
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
