package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.*
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.ui.view.modals.*
import br.com.savedra.challengecrm.viewmodel.*

/**
 * Tela Inicial do Cliente (Caixa de Entrada).
 */
@Composable
fun ClientHomeScreen(
  navController: NavController,
  authViewModel: AuthViewModel,
  onLogoutClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  onChatClick: () -> Unit,
  bannerViewModel: BannerViewModel,
  campaignViewModel: CampaignViewModel
) {
  val currentUser by authViewModel.currentUser.collectAsState()
  val banners by bannerViewModel.filteredBanners.collectAsState()
  val campaigns by campaignViewModel.filteredCampaigns.collectAsState()

  var selectedBanner by remember { mutableStateOf<Banner?>(null) }
  var selectedCampaign by remember { mutableStateOf<Campaign?>(null) }
  var showBannerDetails by remember { mutableStateOf(false) }
  var showCampaignDetails by remember { mutableStateOf(false) }

  LaunchedEffect(currentUser) {
      if (currentUser != null) {
          bannerViewModel.loadBanners(currentUser?.segment)
          campaignViewModel.loadCampaigns(currentUser?.segment)
      }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    bottomBar = {
      BottomNavigationClient(
        onInboxClick = { },
        onLogoutClick = onLogoutClick,
        isInboxActive = true,
        onEventsCenterClick = onEventsCenterClick,
        onBusinessClubClick = onBusinessClubClick,
        onSheratonHotelClick = onSheratonHotelClick
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(16.dp)
    ) {
      Text(
        text = "Bem-vindo, ${currentUser?.name ?: "Cliente"}!",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = slate800
      )
      
      Spacer(modifier = Modifier.height(24.dp))

      Text("Destaques", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800)
      Spacer(modifier = Modifier.height(8.dp))
      if (banners.isEmpty()) {
          Card(modifier = Modifier.fillMaxWidth().height(100.dp), colors = CardDefaults.cardColors(containerColor = white)) {
              Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                  Text("Nenhuma novidade hoje.", color = slate600)
              }
          }
      } else {
          LazyColumn(modifier = Modifier.height(150.dp)) {
              items(banners) { banner ->
                  Card(
                      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { 
                          selectedBanner = banner
                          showBannerDetails = true 
                      }, 
                      colors = CardDefaults.cardColors(containerColor = indigo500)
                  ) {
                      Text(banner.title ?: "", color = white, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                  }
              }
          }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text("Suas Campanhas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800)
      Spacer(modifier = Modifier.height(8.dp))
      LazyColumn(modifier = Modifier.weight(1f)) {
        if (campaigns.isEmpty()) {
          item { Text("Você não possui campanhas ativas.", color = slate600) }
        }
        items(campaigns) { campaign ->
          Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { 
                selectedCampaign = campaign
                showCampaignDetails = true
            },
            colors = CardDefaults.cardColors(containerColor = white),
            elevation = CardDefaults.cardElevation(2.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(text = campaign.title ?: "", fontWeight = FontWeight.Bold, color = slate800)
              Text(text = campaign.description ?: "", color = slate600, fontSize = 14.sp)
            }
          }
        }
      }
      
      Button(
          onClick = { navController.navigate(br.com.savedra.challengecrm.navigation.AppRoutes.OPERATOR_LIST) },
          modifier = Modifier.fillMaxWidth().height(56.dp),
          colors = ButtonDefaults.buttonColors(containerColor = slate800)
      ) {
          Icon(Icons.Default.Chat, contentDescription = null, tint = white)
          Spacer(Modifier.width(8.dp))
          Text("FALAR COM ATENDENTE", fontWeight = FontWeight.Bold, color = white)
      }
    }
  }

  if (showBannerDetails && selectedBanner != null) {
      BannerDetailsModal(banner = selectedBanner!!, onDismiss = { showBannerDetails = false })
  }
  if (showCampaignDetails && selectedCampaign != null) {
      CampaignDetailsModal(campaign = selectedCampaign!!, onDismiss = { showCampaignDetails = false })
  }
}

@Composable
fun BottomNavigationClient(
  onInboxClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  isInboxActive: Boolean = false,
  isEventsCenterActive: Boolean = false,
  isBusinessClubActive: Boolean = false,
  isSheratonHotelActive: Boolean = false,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = slate800,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onInboxClick() }.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Inbox,
          contentDescription = "Início",
          tint = if (isInboxActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Text("Início", color = white, fontSize = 10.sp)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onEventsCenterClick() }.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Event,
          contentDescription = "Eventos",
          tint = if (isEventsCenterActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Text("Eventos", color = white, fontSize = 10.sp)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onBusinessClubClick() }.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Business,
          contentDescription = "Business",
          tint = if (isBusinessClubActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Text("Clube", color = white, fontSize = 10.sp)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSheratonHotelClick() }.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Hotel,
          contentDescription = "Hotel",
          tint = if (isSheratonHotelActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Text("Hotel", color = white, fontSize = 10.sp)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onLogoutClick() }.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Logout,
          contentDescription = "Sair",
          tint = Color.Red,
          modifier = Modifier.size(24.dp)
        )
        Text("Sair", color = white, fontSize = 10.sp)
      }
    }
  }
}
