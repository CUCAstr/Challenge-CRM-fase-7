package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import br.com.savedra.challengecrm.ui.view.components.StandardTextField

/**
 * Modal de Detalhes do Cliente.
 * 
 * CORREÇÕES APLICADAS:
 * 1. Botões alinhados horizontalmente (Row).
 * 2. Uso do StandardTextField para fixar o TAB e o Contraste.
 * 3. Cores de alto contraste em todos os botões.
 */
@Composable
fun CustomerDetailsModal(
    customer: User,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onSaveNotes: (String) -> Unit
) {
    var notes by remember { mutableStateOf(customer.notes ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = customer.name ?: "Sem Nome",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Text(text = customer.email ?: "", fontSize = 14.sp, color = slate600)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Segmento: ${customer.segment ?: "Geral"}", fontSize = 14.sp, color = slate800)
                Text(text = "Status: ${customer.status ?: "Ativo"}", fontSize = 14.sp, color = slate800)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(text = "Notas do Operador", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = slate700)
                Spacer(modifier = Modifier.height(8.dp))
                
                // CORREÇÃO: Uso do componente padrão
                StandardTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Observações",
                    modifier = Modifier.height(120.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CORREÇÃO: Alinhamento Horizontal (Row)
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) { 
                        Text("CANCELAR", color = slate600, fontWeight = FontWeight.Bold) 
                    }
                    Row {
                        Button(
                            onClick = onSendMessage,
                            colors = ButtonDefaults.buttonColors(containerColor = indigo500)
                        ) { 
                            Text("CHAT", color = Color.White, fontWeight = FontWeight.Bold) 
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onSaveNotes(notes) },
                            colors = ButtonDefaults.buttonColors(containerColor = slate800)
                        ) { 
                            Text("SALVAR", color = Color.White, fontWeight = FontWeight.Bold) 
                        }
                    }
                }
            }
        }
    }
}
