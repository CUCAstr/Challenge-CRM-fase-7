package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*

/**
 * Modal de Detalhes do Cliente.
 */
@Composable
fun CustomerDetailsModal(
    customer: User,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onSaveNotes: (String) -> Unit
) {
    // CORREÇÃO: Tratamento de nulos ao inicializar o estado das notas
    var notes by remember { mutableStateOf(customer.notes ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = slate50)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = customer.name ?: "Sem Nome",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Text(text = customer.email ?: "", fontSize = 14.sp, color = slate600)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(text = "Notas do Operador", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = slate700)
                
                // CORREÇÃO: Alto contraste e fundo branco nas notas
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedContainerColor = white,
                        unfocusedContainerColor = white
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onDismiss) { Text("Fechar", color = slate600) }
                    Row {
                        Button(
                            onClick = onSendMessage,
                            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
                        ) { Text("Chat") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onSaveNotes(notes) },
                            colors = ButtonDefaults.buttonColors(containerColor = slate800)
                        ) { Text("Salvar") }
                    }
                }
            }
        }
    }
}
