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

@Composable
fun CustomerDetailsModal(
    customer: User,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onAddNote: (String) -> Unit
) {
    var newNote by remember { mutableStateOf("") }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

                Spacer(modifier = Modifier.height(24.dp))

                // Anotações Section
                Text(
                    text = "Anotações",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Existing Notes
                if (customer.notes.isEmpty()) {
                    Text(
                        text = "Nenhuma anotação ainda.",
                        fontSize = 14.sp,
                        color = slate500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                slate100,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 120.dp)
                    ) {
                        items(customer.notes) { note ->
                            Text(
                                text = note,
                                fontSize = 14.sp,
                                color = slate700,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        slate100,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // New Note Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newNote,
                        onValueChange = { newNote = it },
                        placeholder = { Text("Nova anotação...", color = slate400) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = slate100,
                            unfocusedContainerColor = slate100,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newNote.isNotBlank()) {
                                onAddNote(newNote)
                                newNote = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = slate600
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Salvar",
                            color = white,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onSendMessage,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = purple500
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Enviar Mensagem",
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
