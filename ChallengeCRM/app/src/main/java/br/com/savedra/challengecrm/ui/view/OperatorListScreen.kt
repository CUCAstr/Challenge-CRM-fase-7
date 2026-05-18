package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.UsersViewModel
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.navigation.NavController
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.view.modals.CreateOperatorModal

/**
 * Tela de Lista de Operadores e Canais de Segmento.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorListScreen(
  usersViewModel: UsersViewModel = viewModel(),
  authViewModel: AuthViewModel = viewModel(),
  onOperatorClick: (User) -> Unit,
  navController: NavController,
  onBackClick: () -> Unit // Adicionado parâmetro faltante
) {
  val users by usersViewModel.users.collectAsState()
  val currentUser by authViewModel.currentUser.collectAsState()
  
  // Filtra operadores, tratando nulos com safe call e elvis
  val operators = users.filter { (it.role ?: "") == "OPERATOR" || (it.role ?: "") == "Operador" }
  
  val segments = listOf("ED","IT","Retail & Financial","GRC","HR","Smart Spends","Health","CSC","Field Marketing","Finance","ESG","CX")

  var showCreateOperatorModal by remember { mutableStateOf(false) }

  // --- CORREÇÃO: RECARREGAR DADOS ---
  LaunchedEffect(Unit) {
    usersViewModel.loadUsers()
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    topBar = {
        TopAppBar(
            title = { Text("Operadores e Canais", color = slate800, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) { // Usando o callback correto
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
        )
    },
    containerColor = slate200,
    floatingActionButton = {
      // Apenas operadores podem criar novos operadores
      if (currentUser?.role == "OPERATOR" || currentUser?.role == "Operador") {
        FloatingActionButton(
          onClick = { showCreateOperatorModal = true },
          containerColor = purple500,
          contentColor = white
        ) {
          Icon(Icons.Default.Add, contentDescription = "Novo Operador")
        }
      }
    }
  ) { innerPadding ->
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
      LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          Text(text = "Lista de Operadores", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = slate800)
        }
        items(operators) { operator ->
          OperatorCard(operator = operator, onClick = { onOperatorClick(operator) })
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
          Text(text = "Chats por Segmento", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = slate800)
        }
        items(segments) { segment ->
          SegmentCard(segment = segment) {
            navController.navigate("${AppRoutes.GROUP_CHAT_READONLY}/$segment")
          }
        }
      }
    }
  }

  if (showCreateOperatorModal) {
    CreateOperatorModal(
      onDismiss = { showCreateOperatorModal = false },
      onSuccess = { 
          showCreateOperatorModal = false
          usersViewModel.loadUsers() // Recarrega após sucesso
      }
    )
  }
}

/**
 * Card para canais de segmento.
 */
@Composable
fun SegmentCard(
  segment: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    colors = CardDefaults.cardColors(containerColor = white)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = segment,
        style = MaterialTheme.typography.bodyLarge,
        color = slate800,
        fontWeight = FontWeight.Bold
      )
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Abrir histórico",
        tint = slate600
      )
    }
  }
}

@Composable
fun OperatorCard(operator: User, onClick: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth().clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = white),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier.size(48.dp).clip(CircleShape).background(slate200),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Person, contentDescription = null, tint = slate600)
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        // CORREÇÃO: Tratamento de nulos
        Text(text = operator.name ?: "Operador", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = slate800)
        Text(text = operator.email ?: "", fontSize = 14.sp, color = slate600)
        Text(text = operator.segment ?: "Geral", fontSize = 14.sp, color = slate600)
      }

      Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = slate400, modifier = Modifier.size(20.dp))
    }
  }
}
