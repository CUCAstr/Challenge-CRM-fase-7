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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.Promotion
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.modals.CreatePromotionModal
import br.com.savedra.challengecrm.ui.view.modals.PromotionDetailsModal
import br.com.savedra.challengecrm.viewmodel.PromotionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionsScreen(
  navController: NavController,
  onClientsClick: () -> Unit,
  onInvitesClick: () -> Unit,
  onCampaignsClick: () -> Unit,
  onBannersClick: () -> Unit,
  onChatsClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onBackClick: () -> Unit,
  viewModel: PromotionViewModel
) {
  val promotions by viewModel.filteredPromotions.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showCreatePromotionModal by remember { mutableStateOf(false) }
  var selectedPromotion by remember { mutableStateOf<Promotion?>(null) }
  var showDetailsModal by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
    viewModel.loadPromotions()
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
      TopAppBar(
        title = { Text("Promoções", color = slate800, fontWeight = FontWeight.Bold) },
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
        navController = navController,
        onClientsClick = onClientsClick,
        onInvitesClick = onInvitesClick,
        onPromotionsClick = { },
        onCampaignsClick = onCampaignsClick,
        onBannersClick = onBannersClick,
        onChatsClick = onChatsClick,
        onLogoutClick = onLogoutClick,
        isPromotionsActive = true
      )
    }
  ) { paddingValues ->
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Gestão de Promoções", fontSize = 16.sp, color = slate600, modifier = Modifier.weight(1f))
        IconButton(onClick = { showCreatePromotionModal = true }) { Icon(Icons.Default.Add, contentDescription = null, tint = indigo500) }
      }
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, null, tint = slate400)
            TextField(value = searchQuery, onValueChange = { viewModel.updateSearchQuery(it) }, placeholder = { Text("Pesquisar...", color = slate400) }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black))
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      if (promotions.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Nenhuma promoção encontrada.", color = slate800) }
      } else {
          LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(promotions) { promotion ->
                Card(modifier = Modifier.fillMaxWidth().clickable { selectedPromotion = promotion; showDetailsModal = true }, colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = promotion.title ?: "", fontWeight = FontWeight.Bold, color = slate800)
                        Text(text = promotion.description ?: "", color = slate600, maxLines = 1)
                    }
                }
            }
          }
      }
    }
  }
  if (showCreatePromotionModal) { CreatePromotionModal(onDismiss = { showCreatePromotionModal = false }, viewModel = viewModel) }
  if (showDetailsModal && selectedPromotion != null) { PromotionDetailsModal(promotion = selectedPromotion!!, onDismiss = { showDetailsModal = false; selectedPromotion = null }) }
}
