package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp // CORREÇÃO: Importação faltante
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.dialogs.FilteredClientsDialog
import br.com.savedra.challengecrm.viewmodel.BannerViewModel

/**
 * Modal para criação de novos banners.
 * 
 * CORREÇÕES APLICADAS:
 * 1. Cores de alto contraste e fundo branco forçado.
 * 2. Navegação via Tab (ImeAction.Next).
 * 3. Validação de campos numéricos (apenas dígitos).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBannerModal(
  onDismiss: () -> Unit,
  viewModel: BannerViewModel
) {
  val title by viewModel.newBannerTitle.collectAsState()
  val description by viewModel.newBannerDescription.collectAsState()
  val imageUrl by viewModel.newBannerImageUrl.collectAsState()
  val segmentFilter by viewModel.segmentFilter.collectAsState()
  val statusFilter by viewModel.statusFilter.collectAsState()
  val scoreStartFilter by viewModel.scoreStartFilter.collectAsState()
  val scoreEndFilter by viewModel.scoreEndFilter.collectAsState()
  val showError by viewModel.showError.collectAsState()

  val segments = listOf("Todos", "ED", "IT", "Retail & Financial", "GRC", "HR", "Smart Spends", "Health", "CSC", "Field Marketing", "Finance", "ESG", "CX")
  val statusList = listOf("Todos", "Ativo", "Em negociação", "Inativo")

  var expandedSegment by remember { mutableStateOf(false) }
  var expandedStatus by remember { mutableStateOf(false) }
  var showFilteredClients by remember { mutableStateOf(false) }

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
          Text("Novo Banner", style = MaterialTheme.typography.headlineSmall, color = slate800, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))
        }

        item {
          OutlinedTextField(
            value = title,
            onValueChange = { viewModel.onNewBannerTitleChange(it) },
            label = { Text("Título do Banner", color = slate700) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
            onValueChange = { viewModel.onNewBannerDescriptionChange(it) },
            label = { Text("Descrição", color = slate700) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedContainerColor = white, unfocusedContainerColor = white
            )
          )
        }

        item {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = imageUrl,
            onValueChange = { viewModel.onNewBannerImageUrlChange(it) },
            label = { Text("URL da Imagem", color = slate700) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                focusedContainerColor = white, unfocusedContainerColor = white
            )
          )
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
          HorizontalDivider(color = slate200)
          Spacer(modifier = Modifier.height(16.dp))
          Text("Filtros de Segmentação", style = MaterialTheme.typography.titleMedium, color = slate800, fontWeight = FontWeight.Bold)
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
              label = { Text("Segmento Alvo", color = slate700) },
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
                DropdownMenuItem(text = { Text(s, color = Color.Black) }, onClick = { viewModel.onSegmentFilterChange(s); expandedSegment = false }, modifier = Modifier.background(white))
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = scoreStartFilter,
              onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onScoreStartFilterChange(it) },
              label = { Text("Score Mín", color = slate700) },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
              value = scoreEndFilter,
              onValueChange = { if (it.all { c -> c.isDigit() }) viewModel.onScoreEndFilterChange(it) },
              label = { Text("Score Máx", color = slate700) },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
              colors = OutlinedTextFieldDefaults.colors(
                  focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                  focusedContainerColor = white, unfocusedContainerColor = white
              )
            )
          }
        }

        item {
          Spacer(modifier = Modifier.height(32.dp))
          if (showError) {
              Text("Preencha todos os campos obrigatórios.", color = Color.Red, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(8.dp))
          }
          Button(
            onClick = { viewModel.sendBanner(); onDismiss() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
          ) {
            Text("Criar e Enviar Banner", color = Color.White, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(12.dp))
          TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar", color = slate600)
          }
        }
      }
    }
  }
}
