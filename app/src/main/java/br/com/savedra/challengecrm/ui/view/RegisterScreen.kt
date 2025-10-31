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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

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

  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = slate100)
  ) {
    Column(
      modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      Text(
        text = "Bem-vindo a WTC Connect", style = TextStyle(
          fontFamily = interFamily, fontSize = 24.sp, fontWeight = FontWeight.Bold
        ), modifier = Modifier.padding(bottom = 6.dp)
      )

      Text(
        text = "Crie sua conta para continuar", style = TextStyle(
          fontFamily = interFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal
        ), modifier = Modifier.padding(bottom = 24.dp)
      )

      OutlinedTextField(
        value = name,
        onValueChange = { viewModel.onNameChange(it) },
        label = { Text("Nome Completo") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Unspecified,
          autoCorrectEnabled = true,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Unspecified
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500, unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))


      OutlinedTextField(
        value = email,
        onValueChange = {
          viewModel.onEmailChange(it)
          isError = !isValidEmail(it)
        },
        label = { Text("Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Unspecified,
          autoCorrectEnabled = true,
          keyboardType = KeyboardType.Email,
          imeAction = ImeAction.Unspecified
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500, unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = viewModel.company.collectAsState().value,
        onValueChange = { viewModel.onCompanyChange(it) },
        label = { Text("Empresa") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Unspecified,
          autoCorrectEnabled = true,
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Unspecified
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500, unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

      val genderOptions = listOf("Masculino", "Feminino", "Outro")
      var genderExpanded by remember { mutableStateOf(false) }

      ExposedDropdownMenuBox(
        expanded = genderExpanded,
        onExpandedChange = { genderExpanded = !genderExpanded },
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = viewModel.gender.collectAsState().value,
          onValueChange = { },
          label = { Text("Gênero") },
          readOnly = true,
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
          },
          modifier = Modifier
            .menuAnchor()
            .fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = indigo500, unfocusedBorderColor = slate200
          ),
        )
        ExposedDropdownMenu(
          expanded = genderExpanded,
          onDismissRequest = { genderExpanded = false },
          modifier = Modifier.fillMaxWidth()
        ) {
          genderOptions.forEach { gender ->
            DropdownMenuItem(text = { Text(gender) }, onClick = {
              viewModel.onGenderChange(gender)
              genderExpanded = false
            })
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = viewModel.phone.collectAsState().value,
        onValueChange = { viewModel.onPhoneChange(it) },
        label = { Text("Telefone") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Phone, imeAction = ImeAction.Unspecified
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500, unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))


      val segments = listOf(
        "ED",
        "IT",
        "Retail & Financial",
        "GRC",
        "HR",
        "Smart Spends",
        "Health",
        "CSC",
        "Field Marketing",
        "Finance",
        "ESG",
        "CX"
      )
      var expandedSegment by remember { mutableStateOf(false) }

      ExposedDropdownMenuBox(
        expanded = expandedSegment,
        onExpandedChange = { expandedSegment = !expandedSegment },
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = viewModel.segment.collectAsState().value,
          onValueChange = { },
          label = { Text("Segmento") },
          readOnly = true,
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment)
          },
          modifier = Modifier
            .menuAnchor()
            .fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = indigo500, unfocusedBorderColor = slate200
          ),
        )
        ExposedDropdownMenu(
          expanded = expandedSegment,
          onDismissRequest = { expandedSegment = false },
          modifier = Modifier.fillMaxWidth()
        ) {
          segments.forEach { segment ->
            DropdownMenuItem(text = { Text(segment) }, onClick = {
              viewModel.onSegmentChange(segment)
              expandedSegment = false
            })
          }
        }
      }


      Spacer(modifier = Modifier.height(16.dp))


      OutlinedTextField(
        value = password,
        onValueChange = { viewModel.onPasswordChange(it) },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
          val image = if (passwordVisible) Icons.Filled.Visibility
          else Icons.Filled.VisibilityOff

          val description = if (passwordVisible) "Hide password" else "Show password"

          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(imageVector = image, description)
          }
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500, unfocusedBorderColor = slate200
        ),
        modifier = Modifier.fillMaxWidth()
      )


      Spacer(modifier = Modifier.height(24.dp))


      Button(
        onClick = { viewModel.register() },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
          containerColor = slate600,
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "Realizar cadastro", style = TextStyle(
            fontFamily = interFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal
          )
        )
      }


      Spacer(modifier = Modifier.height(24.dp))


      when (val state = authState) {
        is AuthUIState.Loading -> {
          Toast.makeText(context, "Realizando seu cadastro...", Toast.LENGTH_SHORT).show()
        }

        is AuthUIState.Error -> {
          Text("Erro: ${state.message}", color = MaterialTheme.colorScheme.error)
        }

        is AuthUIState.Success -> {
          LaunchedEffect(state.user) {
            state.user?.let { user ->
              when (user.role) {
                "Cliente" -> navController.navigate(AppRoutes.CLIENT_HOME) {
                  popUpTo(AppRoutes.REGISTER) { inclusive = true }
                }

                "Operador" -> navController.navigate(AppRoutes.OPERATOR_HOME) {
                  popUpTo(AppRoutes.REGISTER) { inclusive = true }
                }
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
fun RegisterScreenPreview() {
  ChallengeCRMTheme {
    RegisterScreen(navController = rememberNavController())
  }
}
