package br.com.savedra.challengecrm.ui.view

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
import androidx.compose.material.icons.filled.PhotoCameraBack
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.Banner
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.modals.CreateBannerModal
import br.com.savedra.challengecrm.viewmodel.BannerViewModel
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannersScreen(
  onClientsClick: () -> Unit = {},
  onInvitesClick: () -> Unit = {},
  onPromotionsClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onChatsClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  viewModel: BannerViewModel = viewModel()
) {
  val banners by viewModel.filteredBanners.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showBannerDetails by remember { mutableStateOf(false) }
  var selectedBanner by remember { mutableStateOf<Banner?>(null) }

  var showCreateBannerModal by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
  }

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
            text = "Painel de Banners",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = slate800
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Gestão de Banners",
            fontSize = 16.sp,
            color = slate600
          )
        }
        IconButton(onClick = { showCreateBannerModal = true }) {
          Icon(Icons.Default.Add, contentDescription = "Criar Banner")
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

      // Banners List
      if (banners.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("Não há nada para listar.")
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(banners) { banner ->
            BannerCard(
              banner = banner,
              onClick = {
                selectedBanner = banner
                showBannerDetails = true
              }
            )
          }
        }
      }
    }

    // Bottom Navigation
    ScrollableBottomNavigation(
      onClientsClick = onClientsClick,
      onInvitesClick = onInvitesClick,
      onPromotionsClick = onPromotionsClick,
      onCampaignsClick = onCampaignsClick,
      onBannersClick = { /* Already on banners screen */ },
      onChatsClick = onChatsClick,
      onLogoutClick = onLogoutClick,
      isBannersActive = true,
      modifier = Modifier.align(Alignment.BottomCenter)
    )

    if (showBannerDetails && selectedBanner != null) {
      BannerDetailsModal(
        banner = selectedBanner!!,
        onDismiss = {
          showBannerDetails = false
          selectedBanner = null
        }
      )
    }

    if (showCreateBannerModal) {
      CreateBannerModal(
        onDismiss = { showCreateBannerModal = false },
        viewModel = viewModel
      )
    }
  }
}

@Composable
fun BannerCard(
  banner: Banner,
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
        Icon(Icons.Default.PhotoCameraBack, contentDescription = "Criar Banner")
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Banner Info
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = banner.title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = banner.description,
          fontSize = 14.sp,
          color = slate600
        )
      }
    }
  }
}

@Composable
fun BannerDetailsModal(
  banner: Banner,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(containerColor = white)
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = banner.title,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = banner.description,
          fontSize = 16.sp,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "URL da imagem: ${banner.imageUrl}",
          fontSize = 14.sp,
          color = slate800
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Fechar")
          }
        }
      }
    }
  }
}
