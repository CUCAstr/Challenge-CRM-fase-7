package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
 */
@Composable
fun CustomerDetailsModal(
    customer: User,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit = {},
    onSaveNotes: (String) -> Unit = {}
) {
    var notes by remember(customer.id) { mutableStateOf(customer.notes ?: "") }
    val isOperator = customer.role == "OPERATOR" || customer.role == "Operador"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                
                if (!isOperator) {
                    Text(text = "Score: ${customer.score ?: 0} pts", fontSize = 14.sp, color = slate800)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = slate200)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(text = "Perfil 360 (Histórico Recente)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = slate800)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val hasHistory = !customer.notes.isNullOrBlank() || (customer.score ?: 0) > 0
                    
                    if (hasHistory) {
                        Column(modifier = Modifier.fillMaxWidth().background(slate50, RoundedCornerShape(8.dp)).padding(12.dp)) {
                            Text("• Última Campanha: Natal 2025", fontSize = 12.sp, color = slate600)
                            Text("• Última Mensagem: ${customer.notes?.take(20) ?: "Sem notas"}", fontSize = 12.sp, color = slate600)
                            Text("• Membro desde: 2024", fontSize = 12.sp, color = slate600)
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxWidth().background(slate50, RoundedCornerShape(8.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                            Text("Novo Usuário: Sem histórico registrado.", fontSize = 12.sp, color = slate400, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Tarefas Abertas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = slate700)
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.fillMaxWidth().background(indigo100.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(12.dp)) {
                        if (hasHistory) {
                            Text("• Enviar proposta comercial", fontSize = 12.sp, color = indigo600)
                            Text("• Agendar call de acompanhamento", fontSize = 12.sp, color = indigo600)
                        } else {
                            Text("• Qualificação inicial", fontSize = 12.sp, color = indigo600)
                            Text("• Atribuir segmento", fontSize = 12.sp, color = indigo600)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(text = "Notas do Operador", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = slate700)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    StandardTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Observações",
                        modifier = Modifier.height(100.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { 
                        Text("FECHAR", color = slate600, fontWeight = FontWeight.Bold) 
                    }
                    
                    if (!isOperator) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onSendMessage,
                            colors = ButtonDefaults.buttonColors(containerColor = indigo500),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) { 
                            Text("CHAT", color = Color.White, fontWeight = FontWeight.Bold) 
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onSaveNotes(notes) },
                            colors = ButtonDefaults.buttonColors(containerColor = slate800),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) { 
                            Text("SALVAR", color = Color.White, fontWeight = FontWeight.Bold) 
                        }
                    }
                }
            }
        }
    }
}
