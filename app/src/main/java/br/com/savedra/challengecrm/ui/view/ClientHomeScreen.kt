package br.com.savedra.challengecrm.ui.view

import CreateInviteModal
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientHomeScreen(
  onMessageClick: (Message) -> Unit = {},
  onLogoutClick: () -> Unit = {},
  onEventsCenterClick: () -> Unit,
  onBusinessClubClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
) {
  var selectedFilter by remember { mutableStateOf("Convites") }
  var expanded by remember { mutableStateOf(false) }
  val options = listOf("Selecione", "Lidas", "Não lidas")
  var selectedOptionText by remember { mutableStateOf(options[0]) }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(slate50)
    ) {
      // Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp)
      ) {
        Text(
          text = "Caixa de Entrada",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Suas comunicações recentes",
          fontSize = 16.sp,
          color = slate600
        )
      }

      // Filter Tabs
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
        //TODO: Lista do Firebase dependendo do filtro
      }
    }

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

private fun formatTimestamp(timestamp: Date): String {
  val now = Date()
  val diff = now.time - timestamp.time
  val days = diff / (24 * 60 * 60 * 1000)

  return when {
    days == 0L -> "Hoje"
    days == 1L -> "Ontem"
    days < 7L -> "${days} dias atrás"
    else -> SimpleDateFormat("dd/MM", Locale.getDefault()).format(timestamp)
  }
}