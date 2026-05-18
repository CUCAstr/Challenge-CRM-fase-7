package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.AuthViewModel
import br.com.savedra.challengecrm.viewmodel.UsersViewModel
import br.com.savedra.challengecrm.ui.view.modals.CustomerDetailsModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorListScreen(
  navController: NavController,
  authViewModel: AuthViewModel,
  usersViewModel: UsersViewModel,
  onOperatorClick: (User) -> Unit,
  onBackClick: () -> Unit
) {
  val allUsers by usersViewModel.users.collectAsState()
  val currentUser by authViewModel.currentUser.collectAsState()
  
  // CORREÇÃO: Diferenciar Papel (O cliente vê todos os operadores, o operador vê seus colegas)
  val isClient = currentUser?.role == "CLIENT" || currentUser?.role == "Cliente"
  val operators = allUsers.filter { (it.role == "OPERATOR" || it.role == "Operador") && it.id != currentUser?.id }
  
  var selectedOperator by remember { mutableStateOf<User?>(null) }
  var showDetails by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(if (isClient) "Nossos Atendentes" else "Equipe de Operadores", color = white) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = white)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = slate800)
      )
    },
    containerColor = slate50
  ) { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
      if (operators.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
          Text("Nenhum operador disponível no momento.", color = slate600)
        }
      } else {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          items(operators) { op ->
            Card(
              modifier = Modifier.fillMaxWidth().clickable { 
                  // CORREÇÃO (Item 2): Se for cliente, abre o CHAT DIRETO. Se for operador, abre DETALHES.
                  if (isClient) {
                      onOperatorClick(op)
                  } else {
                      selectedOperator = op
                      showDetails = true
                  }
              },
              colors = CardDefaults.cardColors(containerColor = white),
              elevation = CardDefaults.cardElevation(2.dp)
            ) {
              Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(CircleShape).background(indigo100), contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Person, contentDescription = null, tint = indigo500)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                  Text(op.name ?: "Atendente", fontWeight = FontWeight.Bold, color = slate800)
                  Text(if (isClient) "Disponível para chat" else (op.email ?: ""), fontSize = 12.sp, color = slate600)
                }
              }
            }
          }
        }
      }
    }
  }

  if (showDetails && selectedOperator != null) {
      CustomerDetailsModal(
          customer = selectedOperator!!,
          onDismiss = { showDetails = false },
          onSendMessage = { 
              showDetails = false
              onOperatorClick(selectedOperator!!)
          },
          onSaveNotes = { /* ... */ }
      )
  }
}
