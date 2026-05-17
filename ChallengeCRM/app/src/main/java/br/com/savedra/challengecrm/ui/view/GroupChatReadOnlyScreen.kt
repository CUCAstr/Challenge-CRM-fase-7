package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

/**
 * Tela de Chat de Grupo (Apenas Leitura).
 * 
 * CORREÇÃO: Utiliza MessageBubble centralizada e trata nulos.
 */
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

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(slate50)) {
        GroupChatHeader(segment = segment)
        
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isCurrentUser = (message.senderId == currentSenderId)
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = slate100,
            tonalElevation = 2.dp
        ) {
            Text(
                text = "Este canal é apenas para leitura.",
                modifier = Modifier.padding(16.dp),
                color = slate600,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
