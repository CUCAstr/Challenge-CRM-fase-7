package br.com.savedra.challengecrm.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.ui.theme.purple100
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate500
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

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

    Column(modifier = Modifier.fillMaxSize()) {
        GroupChatHeader(segment = segment)
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
                    isFromCurrentUser = isFromCurrentUser,
                    isImportant = message.isImportant
                )
            }
        }

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
        modifier = Modifier
            .fillMaxWidth()
            .background(slate200)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Group,
            contentDescription = "Ícone do Grupo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = segment,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = slate800
            )
            Text(
                text = "Chat de grupo",
                fontSize = 14.sp,
                color = slate500
            )
        }
    }
}
