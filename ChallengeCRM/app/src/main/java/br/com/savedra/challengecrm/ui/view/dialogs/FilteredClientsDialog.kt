package br.com.savedra.challengecrm.ui.view.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*

/**
 * Diálogo que exibe a lista de clientes filtrados para uma campanha.
 */
@Composable
fun FilteredClientsDialog(
  clients: List<User>,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      colors = CardDefaults.cardColors(containerColor = white)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Clientes Selecionados", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = slate800)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
          items(clients) { client ->
            // CORREÇÃO: Tratamento de nulo no nome do cliente
            Text(
              text = "• ${client.name ?: "Sem Nome"}",
              modifier = Modifier.padding(vertical = 4.dp),
              color = slate700
            )
          }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = indigo500)
        ) {
          Text("Entendi")
        }
      }
    }
  }
}
