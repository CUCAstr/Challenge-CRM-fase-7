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
import br.com.savedra.challengecrm.viewmodel.InviteViewModel
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitesScreen(
  onClientsClick: () -> Unit = {},
  onPromotionsClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onBannersClick: () -> Unit = {},
  onChatsClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  viewModel: InviteViewModel = viewModel()
) {
  val invites by viewModel.filteredInvites.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showCreateInviteModal by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) { focusManager.clearFocus() }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
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
      Row(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
          Text(text = "Convites", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = slate800)
          Text(text = "Gestão de Convites", fontSize = 16.sp, color = slate600)
        }
        IconButton(onClick = { showCreateInviteModal = true }) {
          Icon(Icons.Default.Add, contentDescription = null, tint = indigo500)
        }
      }

      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white)) {
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

      LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(invites) { invite ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = invite.title, fontWeight = FontWeight.Bold, color = slate800)
                    Text(text = invite.description, color = slate600)
                }
            }
        }
      }
    }
  }

  if (showCreateInviteModal) {
    CreateInviteModal(onDismiss = { showCreateInviteModal = false }, viewModel = viewModel)
  }
}
