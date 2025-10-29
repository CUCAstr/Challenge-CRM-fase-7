import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
  viewModel: ChatViewModel,
  operator: User, //TODO: Talvez de erro
  user: User,
  currentSenderId: String
) {
  LaunchedEffect(Unit) {
    viewModel.loadMessages(operator.id, user.id)
  }

  val messages by viewModel.messages.collectAsState()
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  var text by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      state = listState,
      modifier = Modifier.weight(1f).padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(messages) { message ->
        MessageBubble(
          message = message,
          isFromCurrentUser = message.senderId == currentSenderId
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
fun MessageBubble(message: Message, isFromCurrentUser: Boolean) {
  val alignment = if (isFromCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
  val bubbleColor = if (isFromCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
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
      Icon(Icons.Default.Send, contentDescription = "Enviar")
    }
  }
}