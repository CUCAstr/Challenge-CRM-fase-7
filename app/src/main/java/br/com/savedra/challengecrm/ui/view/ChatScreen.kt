package br.com.savedra.challengecrm.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.purple100
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate500
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.viewmodel.ChatViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

      try {
        listState.animateScrollToItem(messages.size - 1)
      } catch (e: Exception) {
        listState.scrollToItem(messages.size - 1)
      }
    }
  }

  var text by remember { mutableStateOf("") }
  var filteredCommands by remember { mutableStateOf(emptyList<String>()) }

  var messagePendingDeletion by remember { mutableStateOf<Message?>(null) }

  val otherUser = if (currentUserRole == "Cliente") operator else user

  val quickCommands = remember(currentUserRole) {
    QuickCommands(currentUserRole)
  }

  if (messagePendingDeletion != null) {
    DeleteConfirmationDialog(
      messageText = messagePendingDeletion!!.text,
      onConfirmDelete = {
        viewModel.deleteMessage(messagePendingDeletion!!.id)
        messagePendingDeletion = null
      },
      onDismiss = {
        messagePendingDeletion = null
      }
    )
  }

  Column(modifier = Modifier.fillMaxSize()) {
    ChatHeader(operatorName = otherUser.name, operatorEmail = otherUser.email)
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(messages, key = { it.id }) { message ->
        val isFromCurrentUser = message.senderId == currentSenderId

        SwipeableMessageItem(
          isDeleteEnabled = true,
          onDelete = {
            messagePendingDeletion = message
          },
          onMarkImportant = {
            viewModel.markMessageAsImportant(message.id, !message.isImportant)
          }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth(),
            horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
          ) {
            MessageBubble(
              message = message,
              isFromCurrentUser = isFromCurrentUser,
              isImportant = message.isImportant
            )
          }
        }
      }
    }

    AnimatedVisibility(visible = filteredCommands.isNotEmpty()) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .background(slate200.copy(alpha = 0.5f))
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredCommands) { command ->
          SuggestionChip(
            onClick = {
              val template = quickCommands.getTemplate(command)
              if (template != null) {
                text = template
              }
              filteredCommands = emptyList()
            },
            label = { Text(command) }
          )
        }
      }
    }


    MessageInputRow(
      text = text,
      onTextChange = { newText ->
        text = newText
        filteredCommands = quickCommands.getFilteredCommands(newText)
      },
      onSendClick = {
        viewModel.sendMessage(text, currentSenderId, operator, user)
        text = ""
        filteredCommands = emptyList()
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
fun MessageBubble(
  message: Message,
  isFromCurrentUser: Boolean,
  isImportant: Boolean
) {
  val alignment = if (isFromCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
  val bubbleColor = if (isFromCurrentUser) purple100 else slate200
  val shape = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = if (isFromCurrentUser) 16.dp else 0.dp,
    bottomEnd = if (isFromCurrentUser) 0.dp else 16.dp
  )

  Box(
    modifier = Modifier.padding(4.dp),
    contentAlignment = alignment
  ) {
    Surface(
      shape = shape,
      color = bubbleColor
    ) {

      Column(
        modifier = Modifier.padding(
          start = 12.dp,
          end = 12.dp,
          top = 8.dp,
          bottom = if (isImportant) 4.dp else 8.dp
        )
      ) {
        Text(text = message.text)

        if (isImportant) {
          Spacer(modifier = Modifier.height(4.dp))
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorito",
            modifier = Modifier
              .size(16.dp)
              .align(Alignment.End),
            tint = Color(0xFFE91E63)
          )
        }
      }
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
      placeholder = { Text("Digite sua mensagem...") },
      shape = RoundedCornerShape(24.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    IconButton(onClick = onSendClick, enabled = text.isNotBlank()) {
      Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
    }
  }
}

enum class SwipeState {
  IDLE,
  SWIPING,
  SWIPED_START
}

@Composable
fun SwipeableMessageItem(
  modifier: Modifier = Modifier,
  isDeleteEnabled: Boolean,
  onMarkImportant: () -> Unit,
  onDelete: () -> Unit,
  content: @Composable () -> Unit
) {
  val scope = rememberCoroutineScope()
  val offsetX = remember { Animatable(0f) }
  val swipeState = remember { mutableStateOf(SwipeState.IDLE) }
  val density = LocalDensity.current

  val itemWidthPx = with(density) {
    LocalConfiguration.current.screenWidthDp.dp.toPx()
  }

  val thresholdDelete = -itemWidthPx * 0.3f

  var dragJob by remember { mutableStateOf<Job?>(null) }

  LaunchedEffect(swipeState.value) {
    if (swipeState.value == SwipeState.IDLE) {
      offsetX.animateTo(0f, animationSpec = tween(300))
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(Color.Transparent),
    contentAlignment = Alignment.Center
  ) {

    val backgroundColor by animateColorAsState(
      targetValue = when {
        offsetX.value < thresholdDelete && isDeleteEnabled -> Color.Red.copy(alpha = 0.5f)
        offsetX.value < 0 && isDeleteEnabled -> Color.Red.copy(alpha = 0.2f)
        else -> Color.Transparent
      },
      label = "BackgroundColor"
    )

    Row(
      modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)
        .padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (isDeleteEnabled) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Deletar Mensagem",
          tint = Color.Black,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .pointerInput(isDeleteEnabled) {
          coroutineScope {
            launch {
              detectTapGestures(
                onDoubleTap = {
                  scope.launch { onMarkImportant() }
                }
              )
            }

            launch {
              detectHorizontalDragGestures(
                onDragStart = {
                  dragJob?.cancel()
                  swipeState.value = SwipeState.SWIPING
                },
                onHorizontalDrag = { change, dragAmount ->
                  change.consume()
                  dragJob = scope.launch {
                    val dragTarget = (offsetX.value + dragAmount).coerceIn(
                      if (isDeleteEnabled) -itemWidthPx else 0f,
                      0f
                    )
                    offsetX.snapTo(dragTarget)
                  }
                },
                onDragEnd = {
                  dragJob = scope.launch {
                    if (offsetX.value < thresholdDelete && isDeleteEnabled) {
                      swipeState.value = SwipeState.SWIPED_START
                      onDelete()
                    } else {
                      offsetX.animateTo(0f, animationSpec = tween(300))
                      swipeState.value = SwipeState.IDLE
                    }
                  }
                },
                onDragCancel = {
                  dragJob = scope.launch {
                    offsetX.animateTo(0f, animationSpec = tween(300))
                    swipeState.value = SwipeState.IDLE
                  }
                }
              )
            }
          }
        }
    ) {
      content()
    }
  }
}

@Composable
fun DeleteConfirmationDialog(
  messageText: String,
  onConfirmDelete: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Apagar Mensagem") },
    text = {
      Text(
        text = "Tem a certeza que quer apagar esta mensagem?\n\n\"${messageText}\"",
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
      )
    },
    confirmButton = {
      Button(
        onClick = onConfirmDelete,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
      ) {
        Text("Apagar")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar")
      }
    }
  )
}


class QuickCommands(private val userRole: String) {

  private val operatorCommands = mapOf(
    "/promo" to "Olá! Temos uma nova promoção imperdível para você esta semana. Confira!",
    "/agradecer" to "Agradecemos seu contato. Nossa equipe retornará em breve.",
    "/horario" to "Nosso horário de atendimento é de Segunda a Sexta, das 9h às 18h."
  )

  private val clientCommands = mapOf(
    "/ajuda" to "Preciso de ajuda com um problema técnico.",
    "/falar" to "Gostaria de falar com um atendente humano.",
    "/horario" to "Qual o horário de atendimento de vocês?"
  )

  private val commands = if (userRole == "Cliente") clientCommands else operatorCommands

  fun getFilteredCommands(query: String): List<String> {
    if (!query.startsWith("/")) return emptyList()

    if (query == "/") {
      return commands.keys.toList()
    }

    return commands.keys
      .filter { it.startsWith(query, ignoreCase = true) }
      .toList()
  }

  fun getTemplate(command: String): String? = commands[command]
}
