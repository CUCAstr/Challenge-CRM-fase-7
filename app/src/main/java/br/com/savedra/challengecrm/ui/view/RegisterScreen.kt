package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.ChallengeCRMTheme
import br.com.savedra.challengecrm.ui.theme.indigo500
import br.com.savedra.challengecrm.ui.theme.interFamily
import br.com.savedra.challengecrm.ui.theme.slate100
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate600
import br.com.savedra.challengecrm.viewmodel.AuthUIState
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  viewModel: AuthViewModel = viewModel()
) {
  val name by viewModel.name.collectAsState()
  val email by viewModel.email.collectAsState()
  val password by viewModel.password.collectAsState()
  val authState by viewModel.authUiState.collectAsState()

  var isError by remember { mutableStateOf(false) }
  var expanded by remember { mutableStateOf(false) }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = slate100)
  ) {
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Bem-vindo ao CRM",
                style = TextStyle(
                    fontFamily = interFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        item {
            Text(
                text = "Crie sua conta para continuar",
                style = TextStyle(
                    fontFamily = interFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nome Completo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Unspecified, autoCorrectEnabled = true, keyboardType = KeyboardType.Text, imeAction = ImeAction.Unspecified),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = indigo500,
                    unfocusedBorderColor = slate200
                ),
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    isError = !isValidEmail(it)
                },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Unspecified, autoCorrectEnabled = true, keyboardType = KeyboardType.Email, imeAction = ImeAction.Unspecified),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = indigo500,
                    unfocusedBorderColor = slate200
                ),
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            val roles = listOf("Cliente", "Operador")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.role.collectAsState().value,
                    onValueChange = { },
                    label = { Text("Tipo de usuário") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = indigo500,
                        unfocusedBorderColor = slate200
                    ),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    roles.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(role) },
                            onClick = {
                                viewModel.onRoleChange(role)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            val estados = listOf(
                "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará", "Distrito Federal", "Espírito Santo",
                "Goiás", "Maranhão", "Mato Grosso", "Mato Grosso do Sul", "Minas Gerais", "Pará", "Paraíba",
                "Paraná", "Pernambuco", "Piauí", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul",
                "Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"
            )
            var expandedEstados by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedEstados,
                onExpandedChange = { expandedEstados = !expandedEstados },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.state.collectAsState().value,
                    onValueChange = { },
                    label = { Text("Estado") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstados)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = indigo500,
                        unfocusedBorderColor = slate200
                    ),
                )
                ExposedDropdownMenu(
                    expanded = expandedEstados,
                    onDismissRequest = { expandedEstados = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    estados.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                viewModel.onEstadoChange(estado)
                                expandedEstados = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = viewModel.vip.collectAsState().value,
                    onCheckedChange = { viewModel.onVipChange(it) }
                )
                Text("Cliente VIP")
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = indigo500,
                    unfocusedBorderColor = slate200
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = slate600,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Realizar cadastro",
                    style = TextStyle(
                        fontFamily = interFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            when (val state = authState) {
                is AuthUIState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthUIState.Error -> {
                    Text("Erro: ${state.message}", color = MaterialTheme.colorScheme.error)
                }

                is AuthUIState.Success -> {
                    LaunchedEffect(state.role) {
                        when (state.role) {
                            "Cliente" -> navController.navigate(AppRoutes.CLIENT_HOME) {
                                popUpTo(AppRoutes.REGISTER) { inclusive = true }
                            }
                            "Operador" -> navController.navigate(AppRoutes.OPERATOR_HOME) {
                                popUpTo(AppRoutes.REGISTER) { inclusive = true }
                            }
                        }
                    }
                }

                is AuthUIState.Idle -> {}
            }
        }
    }
  }
}

private fun isValidEmail(email: String): Boolean {
  return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
  ChallengeCRMTheme {
    RegisterScreen(navController = rememberNavController())
  }
}
