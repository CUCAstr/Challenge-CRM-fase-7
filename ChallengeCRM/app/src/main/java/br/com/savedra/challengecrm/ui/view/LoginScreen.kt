package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.AuthUIState
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

/**
 * Tela de Login do sistema.
 */
@Composable
fun LoginScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  authViewModel: AuthViewModel
) {
  val email by authViewModel.email.collectAsState()
  val password by authViewModel.password.collectAsState()
  val authState by authViewModel.authUiState.collectAsState()

  var passwordVisible by rememberSaveable { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    authViewModel.resetUiState()
  }

  if (authState is AuthUIState.Success) {
    val user = (authState as AuthUIState.Success).user
    LaunchedEffect(user) {
      user?.let {
        if (it.role == "Operador") {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        } else {
          navController.navigate(AppRoutes.CLIENT_HOME) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        }
      }
    }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate100
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "WTC Connect",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        ),
        modifier = Modifier.padding(bottom = 8.dp)
      )

      Text(
        text = "Faça login para gerenciar suas conexões",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 14.sp,
          color = slate600
        ),
        modifier = Modifier.padding(bottom = 32.dp)
      )

      OutlinedTextField(
        value = email,
        onValueChange = { authViewModel.onEmailChange(it) },
        label = { Text("Email", color = slate700) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate300
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = password,
        onValueChange = { authViewModel.onPasswordChange(it) },
        label = { Text("Senha", color = slate700) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { 
                focusManager.clearFocus()
                authViewModel.login()
            }
        ),
        trailingIcon = {
          val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(imageVector = image, contentDescription = null, tint = slate600)
          }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate300
        )
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = { 
          focusManager.clearFocus()
          authViewModel.login() 
        },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = slate800),
        shape = RoundedCornerShape(8.dp)
      ) {
        if (authState is AuthUIState.Loading) {
          CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
          Text("Entrar", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      TextButton(onClick = { 
        authViewModel.resetUiState()
        navController.navigate(AppRoutes.REGISTER) 
      }) {
        Text("Não possui conta? Cadastre-se", color = indigo600, fontWeight = FontWeight.Medium)
      }

      // --- ATALHOS DE TESTE (Sprint 2 Final) ---
      Spacer(modifier = Modifier.height(32.dp))
      HorizontalDivider(color = slate300)
      Spacer(modifier = Modifier.height(8.dp))
      Text("Atalhos de Teste Rápido", fontSize = 12.sp, color = slate400)
      
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Operadores", fontSize = 10.sp, color = slate600, fontWeight = FontWeight.Bold)
              TextButton(onClick = { quickLogin(authViewModel, "admin@wtc.com", "password123") }) { Text("Admin", fontSize = 12.sp) }
              TextButton(onClick = { quickLogin(authViewModel, "op2@wtc.com", "123") }) { Text("Op 2", fontSize = 12.sp) }
          }
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Clientes", fontSize = 10.sp, color = slate600, fontWeight = FontWeight.Bold)
              TextButton(onClick = { quickLogin(authViewModel, "finance@cliente.com", "password123") }) { Text("Finance", fontSize = 12.sp) }
              TextButton(onClick = { quickLogin(authViewModel, "esg@cliente.com", "password123") }) { Text("ESG", fontSize = 12.sp) }
          }
      }

      if (authState is AuthUIState.Error) {
        Text(
          text = (authState as AuthUIState.Error).message,
          color = Color.Red,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(top = 16.dp),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
    }
  }
}

private fun quickLogin(viewModel: AuthViewModel, email: String, password: String = "password123") {
    viewModel.onEmailChange(email)
    viewModel.onPasswordChange(password)
    viewModel.login()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  ChallengeCRMTheme {
    LoginScreen(navController = rememberNavController(), authViewModel = viewModel())
  }
}
