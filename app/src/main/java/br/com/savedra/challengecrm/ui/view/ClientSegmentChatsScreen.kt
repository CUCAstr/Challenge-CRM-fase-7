package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import AppRoutes

@Composable
fun ClientSegmentChatsScreen(
  navController: NavController
) {
  val segments = listOf(
    "ED","IT","Retail & Financial","GRC","HR","Smart Spends","Health","CSC","Field Marketing","Finance","ESG","CX"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      text = "Chats por Segmento",
      style = MaterialTheme.typography.headlineMedium
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      items(segments) { segment ->
        ClientSegmentChatCard(segment = segment) {
          navController.navigate("${AppRoutes.GROUP_CHAT_READONLY}/$segment")
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSegmentChatCard(
  segment: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    androidx.compose.foundation.layout.Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
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


