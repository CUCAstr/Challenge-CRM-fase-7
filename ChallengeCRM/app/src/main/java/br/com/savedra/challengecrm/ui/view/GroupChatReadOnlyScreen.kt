package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.ChatViewModel

/**
 * Tela de Chat de Grupo (Apenas Leitura).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatReadOnlyScreen(
    viewModel: ChatViewModel,
    segment: String,
    currentSenderId: String,
    onBackClick: () -> Unit
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

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = segment, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = slate800)
                        Text(text = "Canal de Segmento (Leitura)", fontSize = 12.sp, color = slate600)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).background(slate50)) {
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
}
