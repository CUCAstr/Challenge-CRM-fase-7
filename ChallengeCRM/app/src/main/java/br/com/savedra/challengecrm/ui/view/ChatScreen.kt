package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tela de Chat 1:1.
 * 
 * CORREÇÃO: Removidas as funções MessageBubble e MessageInputRow que foram movidas
 * para ChatComponents.kt para evitar erros de duplicidade na compilação.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  operator: User,
  user: User,
  currentSenderId: String,
  currentUserRole: String
) {
  val messages by viewModel.messages.collectAsState()
  var text by remember { mutableStateOf("") }

  // Garantindo strings não nulas para as funções de busca
  val opId = operator.id ?: ""
  val uId = user.id ?: ""

  LaunchedEffect(Unit) {
    viewModel.loadMessages(opId, uId)
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (currentUserRole == "Operador") (user.name ?: "Cliente") else (operator.name ?: "Atendente"),
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = slate800
            )
            Text(
              text = if (currentUserRole == "Operador") "Cliente" else "Suporte WTC",
              fontSize = 12.sp,
              color = slate600
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(slate50)
    ) {
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        items(messages) { message ->
          // MessageBubble agora é importada de ChatComponents.kt
          MessageBubble(message = message, isCurrentUser = message.senderId == currentSenderId)
        }
      }

      // MessageInputRow agora é importada de ChatComponents.kt
      MessageInputRow(
        text = text,
        onTextChange = { text = it },
        onSendClick = {
          if (text.isNotBlank()) {
            viewModel.sendMessage(text, currentSenderId, operator, user)
            text = ""
          }
        }
      )
    }
  }
}
