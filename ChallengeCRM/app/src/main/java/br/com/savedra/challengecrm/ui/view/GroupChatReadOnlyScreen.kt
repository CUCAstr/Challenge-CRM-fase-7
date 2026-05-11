package br.com.savedra.challengecrm.ui.view

import MessageBubble
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

@Composable
fun GroupChatReadOnlyScreen(
  viewModel: ChatViewModel,
  segment: String,
  currentSenderId: String
) {
  LaunchedEffect(Unit) {
    viewModel.loadGroupMessages(segment)
  }

  val messages by viewModel.messages.collectAsState()
  val listState = rememberLazyListState()

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
          isFromCurrentUser = isFromCurrentUser
        )
      }
    }
  }
}


