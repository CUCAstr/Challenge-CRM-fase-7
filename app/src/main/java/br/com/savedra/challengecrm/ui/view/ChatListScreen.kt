package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.model.ChatRoom
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

@Composable
fun ChatListScreen(
  viewModel: ChatViewModel,
  currentUserId: String,
  onChatClick: (chatRoom: ChatRoom) -> Unit
) {
  LaunchedEffect(currentUserId) {
    viewModel.loadChatRooms(currentUserId)
  }

  val chatRooms by viewModel.chatRooms.collectAsState()

  LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(chatRooms) { room ->
      // Determina qual nome exibir (o da "outra" pessoa)
      val chatPartnerName = if (room.operatorId == currentUserId) {
        room.userName
      } else {
        room.operatorName
      }

      ChatRoomItem(
        partnerName = chatPartnerName,
        lastMessage = room.lastMessageText,
        onClick = { onChatClick(room) }
      )
    }
  }
}

@Composable
fun ChatRoomItem(
  partnerName: String,
  lastMessage: String,
  onClick: () -> Unit
) {
  Card (
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 4.dp)
      .clickable(onClick = onClick)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(text = partnerName, fontWeight = FontWeight.Bold)
      Text(text = lastMessage, style = MaterialTheme.typography.bodyMedium)
    }
  }
}