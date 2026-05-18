package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.ui.theme.*

import br.com.savedra.challengecrm.util.FormatUtils

/**
 * Modal de Detalhes do Convite.
 */
@Composable
fun InviteDetailsModal(
    invite: Invite,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = slate50)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = invite.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = invite.description ?: "", fontSize = 16.sp, color = slate700)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // CORREÇÃO: Formatação de Data e Hora para exibição amigável
                DetailItem("Data", FormatUtils.formatDate(invite.date ?: ""))
                DetailItem("Hora", FormatUtils.formatTime(invite.time ?: ""))
                DetailItem("Local", invite.location ?: "")
                DetailItem("Segmento", invite.segment ?: "")

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = indigo500)
                    ) {
                        Text("Fechar")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, color = slate800, fontSize = 14.sp)
        Text(text = value, color = slate600, fontSize = 14.sp)
    }
}
