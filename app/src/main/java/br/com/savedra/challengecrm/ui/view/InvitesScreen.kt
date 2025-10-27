package br.com.savedra.challengecrm.ui.view

import CreateInviteModal
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.InsertInvitation
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.InviteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitesScreen(
  onClientsClick: () -> Unit = {},
  onPromotionsClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onBannersClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  viewModel: InviteViewModel = viewModel()
) {
  val invites by viewModel.filteredInvites.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showInviteDetails by remember { mutableStateOf(false) }
  var selectedInvite by remember { mutableStateOf<Invite?>(null) }

  var showCreateInviteModal by remember { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(slate50)
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Painel de Convites",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = slate800
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Gestão de Convites",
            fontSize = 16.sp,
            color = slate600
          )
        }
        IconButton(onClick = { showCreateInviteModal = true }) {
          Icon(Icons.Default.Add, contentDescription = "Criar Convite")
        }
      }

      // Search Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
      ) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = white)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = slate400,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            TextField(
              value = searchQuery,
              onValueChange = { viewModel.updateSearchQuery(it) },
              placeholder = { Text("Filtrar por título...", color = slate400) },
              modifier = Modifier.fillMaxWidth(),
              keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Unspecified,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Unspecified
              ),
              colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Invites List
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(invites) { invite ->
          InviteCard(
            invite = invite,
            onClick = {
              selectedInvite = invite
              showInviteDetails = true
            }
          )
        }
      }
    }

    // Bottom Navigation
    ScrollableBottomNavigation(
      onClientsClick = onClientsClick,
      onInvitesClick = { /* Already on invites screen */ },
      onPromotionsClick = onPromotionsClick,
      onCampaignsClick = onCampaignsClick,
      onBannersClick = onBannersClick,
      onLogoutClick = onLogoutClick,
      isInvitesActive = true,
      modifier = Modifier.align(Alignment.BottomCenter)
    )

    if (showInviteDetails && selectedInvite != null) {
      InviteDetailsModal(
        invite = selectedInvite!!,
        onDismiss = {
          showInviteDetails = false
          selectedInvite = null
        }
      )
    }

    if (showCreateInviteModal) {
      CreateInviteModal(
        onDismiss = { showCreateInviteModal = false },
        viewModel = viewModel
      )
    }
  }
}

@Composable
fun InviteCard(
  invite: Invite,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = white),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Avatar
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(slate200),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Mail, contentDescription = "Criar Convite")
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Invite Info
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = invite.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = invite.description,
          fontSize = 14.sp,
          color = slate600
        )
      }
    }
  }
}