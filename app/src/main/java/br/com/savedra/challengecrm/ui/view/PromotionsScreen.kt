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
import androidx.compose.material.icons.filled.CardGiftcard
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
import br.com.savedra.challengecrm.model.Promotion
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.PromotionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsScreen(
  onClientsClick: () -> Unit = {},
  onInvitesClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onBannersClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  viewModel: PromotionViewModel = viewModel()
) {
  val promotions by viewModel.filteredPromotions.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showPromotionDetails by remember { mutableStateOf(false) }
  var selectedPromotion by remember { mutableStateOf<Promotion?>(null) }

  var showCreatePromotionModal by remember { mutableStateOf(false) }

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
            text = "Painel de Promoções",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = slate800
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Gestão de Promoções",
            fontSize = 16.sp,
            color = slate600
          )
        }
        IconButton(onClick = { showCreatePromotionModal = true }) {
          Icon(Icons.Default.Add, contentDescription = "Criar Promoção")
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

      // Promotions List
      if (promotions.isEmpty()) {
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
          items(promotions) { promotion ->
            PromotionCard(
              promotion = promotion,
              onClick = {
                selectedPromotion = promotion
                showPromotionDetails = true
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
      onPromotionsClick = { /* Already on promotions screen */ },
      onCampaignsClick = onCampaignsClick,
      onBannersClick = onBannersClick,
      onLogoutClick = onLogoutClick,
      isPromotionsActive = true,
      modifier = Modifier.align(Alignment.BottomCenter)
    )

    if (showPromotionDetails && selectedPromotion != null) {
      PromotionDetailsModal(
        promotion = selectedPromotion!!,
        onDismiss = {
          showPromotionDetails = false
          selectedPromotion = null
        }
      )
    }

    if (showCreatePromotionModal) {
      CreatePromotionModal(
        onDismiss = { showCreatePromotionModal = false },
        viewModel = viewModel
      )
    }
  }
}

@Composable
fun PromotionCard(
  promotion: Promotion,
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
        Icon(Icons.Default.CardGiftcard, contentDescription = "Criar Promoção")
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Promotion Info
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = promotion.title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = promotion.description,
          fontSize = 14.sp,
          color = slate600
        )
      }
    }
  }
}

@Composable
fun PromotionDetailsModal(
  promotion: Promotion,
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
          text = promotion.title,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = promotion.description,
          fontSize = 16.sp,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Valor original: ${promotion.originalValue}",
          fontSize = 14.sp,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Valor da promoção: ${promotion.promotionValue}",
          fontSize = 14.sp,
          color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Expira no dia ${promotion.dateExpiresIn} às ${promotion.hoursExpiresIn}",
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
