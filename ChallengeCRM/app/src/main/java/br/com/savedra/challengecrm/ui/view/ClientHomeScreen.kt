package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun ClientHomeScreen(
  navController: NavController,
  authViewModel: AuthViewModel,
  bannerViewModel: BannerViewModel,
  campaignViewModel: CampaignViewModel,
  chatViewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
  onLogoutClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit
) {
  val currentUser by authViewModel.currentUser.collectAsState()
  val banners by bannerViewModel.filteredBanners.collectAsState()
  val campaigns by campaignViewModel.filteredCampaigns.collectAsState()
  val unreadMap by chatViewModel.unreadCount.collectAsState()

  var selectedBanner by remember { mutableStateOf<Banner?>(null) }
  var selectedCampaign by remember { mutableStateOf<Campaign?>(null) }
  var showBannerDetails by remember { mutableStateOf(false) }
  var showCampaignDetails by remember { mutableStateOf(false) }

  LaunchedEffect(currentUser) {
      if (currentUser != null) {
          bannerViewModel.loadBanners(currentUser?.segment)
          campaignViewModel.loadCampaigns(currentUser?.segment)
          chatViewModel.loadChatRooms(currentUser?.id ?: "")
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
        .verticalScroll(rememberScrollState())
    ) {
      Text(text = "Olá, ${currentUser?.name ?: "Cliente"}!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = slate800)
      
      Spacer(modifier = Modifier.height(24.dp))

      Text("Canal do Segmento (${currentUser?.segment ?: "Geral"})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800)
      Spacer(modifier = Modifier.height(8.dp))
      val segmentRoom = currentUser?.segment ?: "Todos"
      val unreadSegment = unreadMap[segmentRoom] ?: 0
      
      Card(
          modifier = Modifier.fillMaxWidth().clickable { 
              navController.navigate("${AppRoutes.GROUP_CHAT_READONLY}/$segmentRoom")
          },
          colors = CardDefaults.cardColors(containerColor = white),
          elevation = CardDefaults.cardElevation(2.dp)
      ) {
          Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(indigo100), contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Campaign, contentDescription = null, tint = indigo500)
              }
              Spacer(modifier = Modifier.width(16.dp))
              Column(modifier = Modifier.weight(1f)) {
                  Text("Atualizações de ${currentUser?.segment ?: "Geral"}", fontWeight = FontWeight.Bold, color = slate800)
                  Text("Acompanhe as mensagens dos nossos operadores.", fontSize = 12.sp, color = slate600)
              }
              if (unreadSegment > 0) {
                  Badge(containerColor = Color.Red, contentColor = Color.White) { Text(unreadSegment.toString()) }
              }
          }
      }

      Spacer(modifier = Modifier.height(32.dp))

      Text("Destaques", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800)
      Spacer(modifier = Modifier.height(8.dp))
      if (banners.isEmpty()) {
          Text("Nenhuma novidade hoje.", color = slate400, fontSize = 14.sp)
      } else {
          banners.forEach { banner ->
              Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { selectedBanner = banner; showBannerDetails = true }, colors = CardDefaults.cardColors(containerColor = indigo500)) {
                  Text(banner.title ?: "", color = white, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
              }
          }
      }

      Spacer(modifier = Modifier.height(32.dp))

      Text("Campanhas Ativas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = slate800)
      Spacer(modifier = Modifier.height(8.dp))
      campaigns.forEach { campaign ->
          Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedCampaign = campaign; showCampaignDetails = true }, colors = CardDefaults.cardColors(containerColor = white), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(text = campaign.title ?: "", fontWeight = FontWeight.Bold, color = slate800)
              Text(text = campaign.description ?: "", color = slate600, fontSize = 14.sp)
            }
          }
      }
      
      Spacer(modifier = Modifier.height(40.dp))

      Button(
          onClick = { navController.navigate(AppRoutes.OPERATOR_LIST) },
          modifier = Modifier.fillMaxWidth().height(56.dp),
          colors = ButtonDefaults.buttonColors(containerColor = slate800)
      ) {
          Icon(Icons.Default.Chat, contentDescription = null, tint = white)
          Spacer(Modifier.width(8.dp))
          Text("SUPORTE 1:1", fontWeight = FontWeight.Bold, color = white)
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
