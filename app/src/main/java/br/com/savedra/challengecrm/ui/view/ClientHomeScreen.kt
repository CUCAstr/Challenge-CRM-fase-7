package br.com.savedra.challengecrm.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.ui.theme.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.Banner
import br.com.savedra.challengecrm.model.Campaign
import br.com.savedra.challengecrm.model.Invite
import br.com.savedra.challengecrm.model.Promotion
import br.com.savedra.challengecrm.viewmodel.BannerViewModel
import br.com.savedra.challengecrm.viewmodel.CampaignViewModel
import br.com.savedra.challengecrm.viewmodel.InviteViewModel
import br.com.savedra.challengecrm.viewmodel.PromotionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
  onLogoutClick: () -> Unit = {},
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  campaignViewModel: CampaignViewModel = viewModel(),
  bannerViewModel: BannerViewModel = viewModel(),
  inviteViewModel: InviteViewModel = viewModel(),
  promotionViewModel: PromotionViewModel = viewModel(),
  onChatClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Convites") }
    val campaigns by campaignViewModel.filteredCampaigns.collectAsState()
    val banners by bannerViewModel.filteredBanners.collectAsState()
    val invites by inviteViewModel.filteredInvites.collectAsState()
    val promotions by promotionViewModel.filteredPromotions.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
              .fillMaxSize()
              .background(slate50)
        ) {
            Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Caixa de Entrada",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = slate800
                    )
                    Button(
                        onClick = onChatClick,
                        colors = ButtonDefaults.buttonColors(containerColor = purple500)
                    ) {
                        Text(text = "Chat")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Suas comunicações recentes",
                    fontSize = 16.sp,
                    color = slate600
                )
            }

            Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTab(
                    text = "Campanhas",
                    isSelected = selectedFilter == "Campanhas",
                    onClick = { selectedFilter = "Campanhas" }
                )
                FilterTab(
                    text = "Banners",
                    isSelected = selectedFilter == "Banners",
                    onClick = { selectedFilter = "Banners" }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterTab(
                    text = "Convites",
                    isSelected = selectedFilter == "Convites",
                    onClick = { selectedFilter = "Convites" }
                )
                FilterTab(
                    text = "Promoções",
                    isSelected = selectedFilter == "Promoções",
                    onClick = { selectedFilter = "Promoções" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f)
                  .padding(horizontal = 24.dp)
            ) {
                when (selectedFilter) {
                    "Campanhas" -> {
                        items(campaigns) { campaign ->
                            CampaignItem(campaign)
                        }
                    }
                    "Banners" -> {
                        items(banners) { banner ->
                            BannerItem(banner)
                        }
                    }
                    "Convites" -> {
                        items(invites) { invite ->
                            InviteItem(invite)
                        }
                    }
                    "Promoções" -> {
                        items(promotions) { promotion ->
                            PromotionItem(promotion)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        BottomNavigationClient(
            onInboxClick = { },
            onLogoutClick = onLogoutClick,
            isInboxActive = true,
            isEventsCenterActive = false,
            isBusinessClubActive = false,
            isSheratonHotelActive = false,
            modifier = Modifier.align(Alignment.BottomCenter),
            onEventsCenterClick = onEventsCenterClick,
            onBusinessClubClick = onBusinessClubClick,
            onSheratonHotelClick = onSheratonHotelClick
        )
    }
}

@Composable
fun CampaignItem(campaign: Campaign) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = campaign.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = slate800
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = campaign.description,
                fontSize = 14.sp,
                color = slate600
            )
        }
    }
}

@Composable
fun BannerItem(banner: Banner) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = banner.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = slate800
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = banner.description,
                fontSize = 14.sp,
                color = slate600
            )
        }
    }
}

@Composable
fun InviteItem(invite: Invite) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = invite.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = slate800
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = invite.description,
                fontSize = 14.sp,
                color = slate600
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Local: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = slate800
                )
                Text(
                    text = invite.location,
                    fontSize = 14.sp,
                    color = slate600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "Data: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = slate800
                )
                Text(
                    text = invite.date,
                    fontSize = 14.sp,
                    color = slate600
                )
            }
        }
    }
}

@Composable
fun PromotionItem(promotion: Promotion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = promotion.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = slate800
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = promotion.description,
                fontSize = 14.sp,
                color = slate600
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "De: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = slate800
                )
                Text(
                    text = promotion.originalValue,
                    fontSize = 14.sp,
                    color = slate600
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "Por: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = slate800
                )
                Text(
                    text = promotion.promotionValue,
                    fontSize = 14.sp,
                    color = purple500
                )
            }
        }
    }
}

@Composable
fun FilterTab(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier.clickable { onClick() },
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) purple500 else slate200
    )
  ) {
    Text(
      text = text,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
      color = if (isSelected) white else slate700,
      fontSize = 14.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
fun BottomNavigationClient(
  onInboxClick: () -> Unit,
  onLogoutClick: () -> Unit,
  isInboxActive: Boolean = false,
  isEventsCenterActive: Boolean = false,
  isBusinessClubActive: Boolean = false,
  isSheratonHotelActive: Boolean = false,
  modifier: Modifier = Modifier,
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    colors = CardDefaults.cardColors(containerColor = slate800)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Inbox
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onInboxClick() }
      ) {
        Icon(
          imageVector = Icons.Default.Mail,
          contentDescription = "Entrada",
          tint = if (isInboxActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Entrada",
          color = if (isInboxActive) purple500 else white,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium
        )
      }

      // Events Center
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onEventsCenterClick() }
      ) {
        Icon(
          imageVector = Icons.Default.DateRange,
          contentDescription = "Events Center",
          tint = if (isEventsCenterActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Events\nCenter",
          color = if (isEventsCenterActive) purple500 else white,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium
        )
      }

      // Business Club
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onBusinessClubClick() }
      ) {
        Icon(
          imageVector = Icons.Default.Business,
          contentDescription = "Business Club",
          tint = if (isBusinessClubActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Business\nClub",
          color = if (isBusinessClubActive) purple500 else white,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium
        )
      }

      // Sheraton Hotel
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onSheratonHotelClick() }
      ) {
        Icon(
          imageVector = Icons.Default.Home,
          contentDescription = "Sheraton Hotel",
          tint = if (isSheratonHotelActive) purple500 else white,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Sheraton\nHotel",
          color = if (isSheratonHotelActive) purple500 else white,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium
        )
      }

      // Logout
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onLogoutClick() }
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ExitToApp,
          contentDescription = "Sair",
          tint = white,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Sair",
          color = white,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}
