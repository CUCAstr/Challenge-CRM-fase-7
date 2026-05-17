package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

/**
 * Tela de Chat de Grupo.
 * 
 * CORREÇÃO: Utiliza os componentes centralizados (MessageBubble, MessageInputRow)
 * importados automaticamente via pacote .ui.view.
 */
@Composable
fun GroupChatScreen(
    viewModel: ChatViewModel,
    segment: String,
    currentSenderId: String
) {
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.loadGroupMessages(segment)
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

    Column(modifier = Modifier.fillMaxSize().background(slate50)) {
        GroupChatHeader(segment = segment)
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                // MessageBubble agora é um componente centralizado
                MessageBubble(
                    message = message,
                    isCurrentUser = (message.senderId == currentSenderId)
                )
            }
        }

        // MessageInputRow agora é um componente centralizado
        MessageInputRow(
            text = text,
            onTextChange = { text = it },
            onSendClick = {
                viewModel.sendGroupMessage(text, currentSenderId, segment)
                text = ""
            }
        )
    }
}

@Composable
fun GroupChatHeader(segment: String) {
    Row(
        modifier = Modifier.fillMaxWidth().background(slate200).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Group,
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape).background(slate300)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = segment, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = slate800)
            Text(text = "Canal de Segmento", fontSize = 14.sp, color = slate600)
        }
    }
}
