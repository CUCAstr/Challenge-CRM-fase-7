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
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.AuthUIState
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

/**
 * Modal para criação de novos operadores (Acesso restrito).
 * 
 * CORREÇÕES APLICADAS:
 * 1. Cores de alto contraste (Fundo Branco, Texto Preto).
 * 2. Menu suspenso com fundo forçado para evitar tema escuro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOperatorModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val segment by viewModel.segment.collectAsState()
    val authState by viewModel.authUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onRoleChange("Operador")
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = white) // CORREÇÃO: Fundo Branco
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Novo Operador",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = { Text("Nome Completo", color = slate700) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                        focusedContainerColor = white, unfocusedContainerColor = white
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    label = { Text("Email", color = slate700) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                        focusedContainerColor = white, unfocusedContainerColor = white
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Senha", color = slate700) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                        focusedContainerColor = white, unfocusedContainerColor = white
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                val segments = listOf("ED", "IT", "Finance", "ESG", "CX")
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = segment,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Segmento Responsável", color = slate700) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                            focusedContainerColor = white, unfocusedContainerColor = white
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded, 
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(white) // CORREÇÃO: Fundo dropdown
                    ) {
                        segments.forEach { seg ->
                            DropdownMenuItem(
                                text = { Text(seg, color = Color.Black) }, 
                                onClick = {
                                    viewModel.onSegmentChange(seg)
                                    expanded = false
                                },
                                modifier = Modifier.background(white)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = slate600)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { 
                            viewModel.onCompanyChange("WTC")
                            viewModel.onGenderChange("Outro")
                            viewModel.onPhoneChange("0000000000")
                            viewModel.register() 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = indigo500)
                    ) {
                        if (authState is AuthUIState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = white, strokeWidth = 2.dp)
                        } else {
                            Text("Criar")
                        }
                    }
                }

                if (authState is AuthUIState.Success) {
                    LaunchedEffect(Unit) {
                        viewModel.resetUiState()
                        onSuccess()
                    }
                }
                
                if (authState is AuthUIState.Error) {
                    Text(
                        text = (authState as AuthUIState.Error).message,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
