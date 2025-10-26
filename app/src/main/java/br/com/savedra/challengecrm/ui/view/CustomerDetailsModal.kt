package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomerDetailsModal(
    customer: User,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onSaveNotes: (String) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var notes by remember { mutableStateOf(customer.notes) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalhes do Cliente",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = slate800
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() },
                        tint = slate400
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Informações Section
                Text(
                    text = "Informações",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Nome: ${customer.name}",
                    fontSize = 14.sp,
                    color = slate700
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Email: ${customer.email}",
                    fontSize = 14.sp,
                    color = slate700
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Membro desde: ${customer.memberSince?.let { dateFormat.format(it) } ?: "N/A"}",
                    fontSize = 14.sp,
                    color = slate700
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Estado: ${customer.estado}",
                    fontSize = 14.sp,
                    color = slate700
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VIP: ${if (customer.vip) "Sim" else "Não"}",
                    fontSize = 14.sp,
                    color = slate700
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Anotações") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = { onSaveNotes(notes) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purple500
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Salvar",
                            color = white,
                            fontSize = 14.sp
                        )
                    }
                    Button(
                        onClick = onSendMessage,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purple500
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Chat",
                            color = white,
                            fontSize = 14.sp
                        )
                    }
                    
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = slate600
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Fechar",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
