package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.savedra.challengecrm.navigation.AppRoutes

@Composable
fun ClientChatEntryScreen(
  navController: NavController
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Como você quer conversar?",
      style = MaterialTheme.typography.headlineSmall
    )

    Button(
      onClick = { navController.navigate(AppRoutes.OPERATOR_LIST) },
      modifier = Modifier.fillMaxWidth()
    ) { Text("Operadores") }

    Button(
      onClick = { navController.navigate(AppRoutes.CLIENT_SEGMENT_CHATS) },
      modifier = Modifier.fillMaxWidth()
    ) { Text("Grupo/Segmento") }
  }
}


