package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate400
import br.com.savedra.challengecrm.ui.theme.slate600
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.ui.theme.white
import br.com.savedra.challengecrm.viewmodel.UsersViewModel

import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.navigation.NavController
import AppRoutes

@Composable
fun OperatorListScreen(
  usersViewModel: UsersViewModel = viewModel(),
  onOperatorClick: (User) -> Unit,
  navController: NavController
) {
  val users by usersViewModel.users.collectAsState()
  val operators = users.filter { it.role == "Operador" }
  val segments = listOf(
    "ED","IT","Retail & Financial","GRC","HR","Smart Spends","Health","CSC","Field Marketing","Finance","ESG","CX"
  )

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(slate200)
        .padding(16.dp)
  ) {
    LazyColumn(
      modifier = Modifier
          .fillMaxSize()
          .padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        Text(
          text = "Lista de Operadores",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
        )
      }
      items(operators) { operator ->
        OperatorCard(operator = operator, onClick = { onOperatorClick(operator) })
      }
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
      item {
        Text(
          text = "Chats por Segmento",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
        )
      }
      items(segments) { segment ->
        SegmentCard(segment = segment) {
          navController.navigate("${AppRoutes.GROUP_CHAT_READONLY}/$segment")
        }
      }
    }
  }
}

@Composable
fun SegmentCard(
  segment: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
        style = MaterialTheme.typography.bodyLarge
      )
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = "Abrir histórico"
      )
    }
  }
}

@Composable
fun OperatorCard(operator: User, onClick: () -> Unit) {
  Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = white),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(slate200),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Person, contentDescription = "Criar Convite")
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = operator.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = operator.email,
          fontSize = 14.sp,
          color = slate600
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = operator.segment,
          fontSize = 14.sp,
          color = slate600
        )
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.Chat,
        contentDescription = "Enviar mensagem",
        tint = slate400,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
