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

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  viewModel: AuthViewModel = viewModel()
) {
  val email by viewModel.email.collectAsState()
  val password by viewModel.password.collectAsState()
  val authState by viewModel.authUiState.collectAsState()

  var isError by remember { mutableStateOf(false) }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  val context = LocalContext.current

  Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(color = slate100)
  ) {
    Column(
      modifier = modifier
          .fillMaxSize()
          .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Bem-vindo a WTC",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(bottom = 6.dp)
      )

      Text(
        text = "Faça login para continuar",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(bottom = 24.dp)
      )

      OutlinedTextField(
        value = email,
        onValueChange = {
          viewModel.onEmailChange(it)
          isError = !isValidEmail(it)
        },
        label = { Text("Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = isError,
        modifier = Modifier
          .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

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

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = { viewModel.login() },
        modifier = Modifier
          .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
          containerColor = slate600,
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "Entrar",
          style = TextStyle(
            fontFamily = interFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
          )
        )
      }

      TextButton(
        onClick = { navController.navigate(AppRoutes.REGISTER) },
      ) {
        Text(
          text = "Não tem uma conta? Registre-se",
          style = TextStyle(
            fontFamily = interFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
          ),
          color = indigo500
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      when (val state = authState) {
        is AuthUIState.Loading -> {
          Toast.makeText(context, "Realizando seu login...", Toast.LENGTH_SHORT).show()
        }

        is AuthUIState.Error -> {
          Text("Erro: ${state.message}", color = MaterialTheme.colorScheme.error)
        }

        is AuthUIState.Success -> {
          LaunchedEffect(state.role) {
            when (state.role) {
              "Cliente" -> navController.navigate(AppRoutes.CLIENT_HOME) {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
              }
              "Operador" -> navController.navigate(AppRoutes.OPERATOR_HOME) {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
              }
            }
          }
        }

        is AuthUIState.Idle -> {}
      }
    }
  }
}

private fun isValidEmail(email: String): Boolean {
  return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  ChallengeCRMTheme {
    LoginScreen(navController = rememberNavController())
  }
}