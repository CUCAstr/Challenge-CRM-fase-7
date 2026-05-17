package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.modals.CreateInviteModal
import br.com.savedra.challengecrm.ui.view.modals.InviteDetailsModal
import br.com.savedra.challengecrm.viewmodel.InviteViewModel
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Tela de Gestão de Convites.
 * 
 * CORREÇÕES:
 * 1. Botão Voltar na TopAppBar.
 * 2. Recarregamento de dados ao entrar na tela.
 * 3. Modal de detalhes ao clicar no convite.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitesScreen(
  onClientsClick: () -> Unit = {},
  onPromotionsClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onBannersClick: () -> Unit = {},
  onChatsClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  onBackClick: () -> Unit = {},
  viewModel: InviteViewModel = viewModel()
) {
  val invites by viewModel.filteredInvites.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  
  var showCreateInviteModal by remember { mutableStateOf(false) }
  var selectedInvite by remember { mutableStateOf<Invite?>(null) }
  var showDetailsModal by remember { mutableStateOf(false) }
  
  val focusManager = LocalFocusManager.current

  // --- CORREÇÃO: RECARREGAR DADOS ---
  LaunchedEffect(Unit) {
    focusManager.clearFocus()
    viewModel.loadInvites()
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
      TopAppBar(
        title = { Text("Convites", color = slate800, fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
      )
    },
    bottomBar = {
      ScrollableBottomNavigation(
        onClientsClick = onClientsClick,
        onInvitesClick = { },
        onPromotionsClick = onPromotionsClick,
        onCampaignsClick = onCampaignsClick,
        onBannersClick = onBannersClick,
        onChatsClick = onChatsClick,
        onLogoutClick = onLogoutClick,
        isInvitesActive = true
      )
    }
  ) { paddingValues ->
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      // Header Info
      Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Gestão de Convites", fontSize = 16.sp, color = slate600, modifier = Modifier.weight(1f))
        IconButton(onClick = { showCreateInviteModal = true }) {
          Icon(Icons.Default.Add, contentDescription = null, tint = indigo500)
        }
      }

      // Search Bar
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(2.dp)) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, null, tint = slate400)
            TextField(
              value = searchQuery,
              onValueChange = { viewModel.updateSearchQuery(it) },
              placeholder = { Text("Pesquisar...", color = slate400) },
              modifier = Modifier.fillMaxWidth(),
              colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Lista de Convites
      if (invites.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text("Nenhum convite encontrado.", color = slate800)
          }
      } else {
          LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(invites) { invite ->
                Card(
                  modifier = Modifier.fillMaxWidth().clickable { 
                      selectedInvite = invite
                      showDetailsModal = true
                  }, 
                  colors = CardDefaults.cardColors(containerColor = white),
                  elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = invite.title, fontWeight = FontWeight.Bold, color = slate800)
                        Text(text = invite.description, color = slate600, maxLines = 1)
                    }
                }
            }
          }
      }
    }
  }

  if (showCreateInviteModal) {
    CreateInviteModal(onDismiss = { showCreateInviteModal = false }, viewModel = viewModel)
  }

  if (showDetailsModal && selectedInvite != null) {
    InviteDetailsModal(invite = selectedInvite!!, onDismiss = { showDetailsModal = false; selectedInvite = null })
  }
}
