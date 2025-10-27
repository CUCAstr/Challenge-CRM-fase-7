package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.ui.theme.slate100
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.ui.theme.white

@Composable
fun InviteDetailsModal(
    invite: Invite,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = invite.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = invite.description,
                    fontSize = 16.sp,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Data: ${invite.date}",
                    fontSize = 14.sp,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Local: ${invite.location}",
                    fontSize = 14.sp,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Fechar")
                    }
                }
            }
        }
    }
}