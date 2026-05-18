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
import android.util.Log

/**
 * Tela de Chat 1:1.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  operator: User,
  user: User,
  currentSenderId: String,
  currentUserRole: String,
  onBackClick: () -> Unit
) {
  val messages by viewModel.messages.collectAsState()
  var text by remember { mutableStateOf("") }

  // --- CORREÇÃO: LOG DE DEPURAÇÃO E RECARREGAMENTO ---
  LaunchedEffect(operator.id, user.id) {
    Log.d("ChatScreen", "Entrando no chat: Op=${operator.name} (${operator.id}) | User=${user.name} (${user.id})")
    viewModel.loadMessages(operator, user)
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (currentUserRole == "Operador" || currentUserRole == "OPERATOR") (user.name ?: "Cliente") else (operator.name ?: "Atendente"),
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = slate800
            )
            Text(
              text = if (currentUserRole == "Operador" || currentUserRole == "OPERATOR") "Conversa Direta" else "Suporte WTC",
              fontSize = 12.sp,
              color = slate600
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = slate800)
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
      if (messages.isEmpty()) {
          Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
              Text("Nenhuma mensagem por aqui ainda.", color = slate400)
          }
      } else {
          LazyColumn(
            modifier = Modifier
              .weight(1f)
              .fillMaxWidth()
              .padding(horizontal = 16.dp)
          ) {
            items(messages) { message ->
              MessageBubble(message = message, isCurrentUser = message.senderId == currentSenderId)
            }
          }
      }

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
