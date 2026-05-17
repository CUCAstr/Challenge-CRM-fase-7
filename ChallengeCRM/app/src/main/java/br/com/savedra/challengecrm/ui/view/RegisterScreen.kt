package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.AuthUIState
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

/**
 * Tela de Cadastro de Usuário (Cliente).
 * 
 * CORREÇÕES APLICADAS:
 * 1. Implementada a navegação automática após sucesso.
 * 2. Corrigido contraste em dropdowns e campos de texto.
 * 3. Adicionado Scroll para telas pequenas.
 */
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
  val company by viewModel.company.collectAsState()
  val gender by viewModel.gender.collectAsState()
  val phone by viewModel.phone.collectAsState()
  val segment by viewModel.segment.collectAsState()
  val authState by viewModel.authUiState.collectAsState()

  var passwordVisible by rememberSaveable { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  // Inicializa o estado ao abrir a tela
  LaunchedEffect(Unit) {
    viewModel.resetUiState()
    viewModel.onRoleChange("Cliente")
  }

  // --- LÓGICA DE NAVEGAÇÃO ---
  if (authState is AuthUIState.Success) {
    val user = (authState as AuthUIState.Success).user
    LaunchedEffect(user) {
      user?.let {
        if (it.role == "Operador") {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.REGISTER) { inclusive = true }
          }
        } else {
          navController.navigate(AppRoutes.CLIENT_HOME) {
            popUpTo(AppRoutes.REGISTER) { inclusive = true }
          }
        }
      }
    }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    topBar = {
      TopAppBar(
        title = { Text("Novo Cadastro", color = slate800, fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = slate100)
      )
    },
    containerColor = slate100
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      Text(
        text = "Informações Pessoais",
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800),
        modifier = Modifier.padding(bottom = 16.dp)
      )

      // Nome Completo
      OutlinedTextField(
        value = name,
        onValueChange = { viewModel.onNameChange(it) },
        label = { Text("Nome Completo", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Email
      OutlinedTextField(
        value = email,
        onValueChange = { viewModel.onEmailChange(it) },
        label = { Text("Email", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Empresa
      OutlinedTextField(
        value = company,
        onValueChange = { viewModel.onCompanyChange(it) },
        label = { Text("Empresa", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Gênero Dropdown
      var genderExpanded by remember { mutableStateOf(false) }
      ExposedDropdownMenuBox(
        expanded = genderExpanded,
        onExpandedChange = { genderExpanded = !genderExpanded }
      ) {
        OutlinedTextField(
          value = gender,
          onValueChange = {},
          readOnly = true,
          label = { Text("Gênero", color = slate700) },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
          modifier = Modifier.menuAnchor().fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
          )
        )
        ExposedDropdownMenu(
          expanded = genderExpanded,
          onDismissRequest = { genderExpanded = false },
          modifier = Modifier.background(Color.White)
        ) {
          listOf("Masculino", "Feminino", "Outro").forEach { g ->
            DropdownMenuItem(
              text = { Text(g, color = Color.Black) },
              onClick = { viewModel.onGenderChange(g); genderExpanded = false },
              modifier = Modifier.background(Color.White)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Telefone
      OutlinedTextField(
        value = phone,
        onValueChange = { viewModel.onPhoneChange(it) },
        label = { Text("Telefone", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Segmento Dropdown
      var segmentExpanded by remember { mutableStateOf(false) }
      ExposedDropdownMenuBox(
        expanded = segmentExpanded,
        onExpandedChange = { segmentExpanded = !segmentExpanded }
      ) {
        OutlinedTextField(
          value = segment,
          onValueChange = {},
          readOnly = true,
          label = { Text("Segmento de Interesse", color = slate700) },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = segmentExpanded) },
          modifier = Modifier.menuAnchor().fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
          )
        )
        ExposedDropdownMenu(
          expanded = segmentExpanded,
          onDismissRequest = { segmentExpanded = false },
          modifier = Modifier.background(Color.White)
        ) {
          listOf("ED", "IT", "Finance", "ESG", "CX").forEach { s ->
            DropdownMenuItem(
              text = { Text(s, color = Color.Black) },
              onClick = { viewModel.onSegmentChange(s); segmentExpanded = false },
              modifier = Modifier.background(Color.White)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Senha
      OutlinedTextField(
        value = password,
        onValueChange = { viewModel.onPasswordChange(it) },
        label = { Text("Defina uma Senha", color = slate700) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
          val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(imageVector = image, contentDescription = null, tint = slate600)
          }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
          focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Botão Registrar
      Button(
        onClick = { 
          focusManager.clearFocus()
          viewModel.register() 
        },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = slate800),
        shape = RoundedCornerShape(8.dp)
      ) {
        if (authState is AuthUIState.Loading) {
          CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
          Text("Finalizar Cadastro", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }

      // Feedback de Erro
      if (authState is AuthUIState.Error) {
        Text(
          text = (authState as AuthUIState.Error).message,
          color = Color.Red,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(top = 16.dp),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
  ChallengeCRMTheme {
    RegisterScreen(navController = rememberNavController())
  }
}
