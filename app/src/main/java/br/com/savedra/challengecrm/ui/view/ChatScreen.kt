import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate500
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

import androidx.compose.ui.platform.LocalFocusManager
import br.com.savedra.challengecrm.ui.theme.purple100

@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  operator: User,
  user: User,
  currentSenderId: String,
  currentUserRole: String
) {
  val focusManager = LocalFocusManager.current
  LaunchedEffect(Unit) {
    viewModel.loadMessages(operator.id, user.id)
    focusManager.clearFocus()
  }

  val messages by viewModel.messages.collectAsState()
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  var text by remember { mutableStateOf("") }

  val otherUser = if (currentUserRole == "Cliente") operator else user

  Column(modifier = Modifier.fillMaxSize()) {
    ChatHeader(operatorName = otherUser.name, operatorEmail = otherUser.email)
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(messages) { message ->
        val isFromCurrentUser = message.senderId == currentSenderId
        MessageBubble(
          message = message,
          isFromCurrentUser = isFromCurrentUser
        )
      }
    }

    MessageInputRow(
      text = text,
      onTextChange = { text = it },
      onSendClick = {
        viewModel.sendMessage(text, currentSenderId, operator, user)
        text = ""
      }
    )
  }
}

@Composable
fun ChatHeader(operatorName: String, operatorEmail: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(slate200)
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.Person,
      contentDescription = "Foto do Operador",
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
    )
    Spacer(modifier = Modifier.width(16.dp))
    Column {
      Text(
        text = operatorName,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = slate800
      )
      Text(
        text = operatorEmail,
        fontSize = 14.sp,
        color = slate500
      )
    }
  }
}

@Composable
fun MessageBubble(message: Message, isFromCurrentUser: Boolean) {
  val alignment = if (isFromCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
  val bubbleColor = if (isFromCurrentUser) purple100 else slate200
  val shape = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = if (isFromCurrentUser) 16.dp else 0.dp,
    bottomEnd = if (isFromCurrentUser) 0.dp else 16.dp
  )

  Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = alignment
  ) {
    Surface(
      shape = shape,
      color = bubbleColor,
      modifier = Modifier.padding(4.dp)
    ) {
      Text(
        text = message.text,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
      )
    }
  }
}

@Composable
fun MessageInputRow(
  text: String,
  onTextChange: (String) -> Unit,
  onSendClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    OutlinedTextField(
      value = text,
      onValueChange = onTextChange,
      modifier = Modifier.weight(1f),
      placeholder = { Text("Digite uma mensagem...") },
      shape = RoundedCornerShape(24.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    IconButton(onClick = onSendClick, enabled = text.isNotBlank()) {
      Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
    }
  }
}
