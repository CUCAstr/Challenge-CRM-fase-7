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
import br.com.savedra.challengecrm.model.Campaign
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.modals.CreateCampaignModal
import br.com.savedra.challengecrm.viewmodel.CampaignViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignsScreen(
  navController: NavController,
  onClientsClick: () -> Unit,
  onInvitesClick: () -> Unit,
  onPromotionsClick: () -> Unit,
  onBannersClick: () -> Unit,
  onChatsClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onBackClick: () -> Unit,
  viewModel: CampaignViewModel
) {
  val campaigns by viewModel.filteredCampaigns.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showCreateCampaignModal by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
    viewModel.loadCampaigns()
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
        TopAppBar(
            title = { Text("Campanhas", color = slate800, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
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
        onPromotionsClick = onPromotionsClick,
        onCampaignsClick = { },
        onBannersClick = onBannersClick,
        onChatsClick = onChatsClick,
        onLogoutClick = onLogoutClick,
        isCampaignsActive = true
      )
    }
  ) { paddingValues ->
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
      Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Gestão de Campanhas", fontSize = 16.sp, color = slate600, modifier = Modifier.weight(1f))
        IconButton(onClick = { showCreateCampaignModal = true }) { Icon(Icons.Default.Add, contentDescription = null, tint = indigo500) }
      }
      Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, null, tint = slate400)
            TextField(value = searchQuery, onValueChange = { viewModel.updateSearchQuery(it) }, placeholder = { Text("Pesquisar campanhas...", color = slate400) }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black))
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      if (campaigns.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Nenhuma campanha encontrada.", color = slate800) }
      } else {
          LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(campaigns) { campaign ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = campaign.title ?: "", fontWeight = FontWeight.Bold, color = slate800)
                        Text(text = campaign.description ?: "", color = slate600, maxLines = 2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Segmento: ${campaign.segment ?: ""}", fontSize = 12.sp, color = indigo600)
                    }
                }
            }
          }
      }
    }
  }
  if (showCreateCampaignModal) { CreateCampaignModal(onDismiss = { showCreateCampaignModal = false }, viewModel = viewModel) }
}
