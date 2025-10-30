package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Campaign
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
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.OperatorViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCameraBack
import br.com.savedra.challengecrm.ui.view.modals.CustomerDetailsModal
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@Composable
fun OperatorHomeScreen(
  onCustomerClick: (User) -> Unit = {},
  onChatsClick: () -> Unit = {},
  onInvitesClick: () -> Unit = {},
  onPromotionsClick: () -> Unit = {},
  onCampaignsClick: () -> Unit = {},
  onBannersClick: () -> Unit = {},
  onLogoutClick: () -> Unit = {},
  viewModel: OperatorViewModel = viewModel()
) {
  val customers by viewModel.filteredCustomers.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  var showCustomerDetails by remember { mutableStateOf(false) }
  var selectedCustomer by remember { mutableStateOf<User?>(null) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    viewModel.updateSegmentFilter("Todos")
    viewModel.updateStatusFilter("Todos")
    focusManager.clearFocus()
  }

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
          text = "Painel do Operador",
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Gestão de Clientes",
          fontSize = 16.sp,
          color = slate600
        )
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
              placeholder = { Text("Filtrar por nome...", color = slate400) },
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Filtro por segmento",
              color = slate600,
              fontSize = 12.sp,
              modifier = Modifier.padding(bottom = 4.dp)
            )
            var expandedSegment by remember { mutableStateOf(false) }
            val itemsSegment = listOf(
              "Todos",
              "ED",
              "IT",
              "Retail & Financial",
              "GRC",
              "HR",
              "Smart Spends",
              "Health",
              "CSC",
              "Field Marketing",
              "Finance",
              "ESG",
              "CX"
            )
            var selectedSegment by remember { mutableStateOf(itemsSegment[0]) }

            ExposedDropdownMenuBox(
              expanded = expandedSegment,
              onExpandedChange = { expandedSegment = !expandedSegment }
            ) {
              Card(
                modifier = Modifier.menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = white)
              ) {
                Row(
                  modifier = Modifier.padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = selectedSegment,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1
                  )
                  ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment)
                }
              }

              ExposedDropdownMenu(
                expanded = expandedSegment,
                onDismissRequest = { expandedSegment = false })
              {
                itemsSegment.forEach { item ->
                  DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                      selectedSegment = item
                      expandedSegment = false
                      viewModel.updateSegmentFilter(item)
                    }
                  )
                }
              }
            }
          }

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Filtro por status",
              color = slate600,
              fontSize = 12.sp,
              modifier = Modifier.padding(bottom = 4.dp)
            )
            var expandedStatus by remember { mutableStateOf(false) }
            val itemsStatus = listOf(
              "Todos", "Ativo", "Em negociação", "Inativo", "Aguardando resposta"
            )
            var selectedStatus by remember { mutableStateOf(itemsStatus[0]) }

            ExposedDropdownMenuBox(
              expanded = expandedStatus,
              onExpandedChange = { expandedStatus = !expandedStatus }
            ) {
              Card(
                modifier = Modifier.menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = white)
              ) {
                Row(
                  modifier = Modifier.padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = selectedStatus,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1
                  )
                  ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                }
              }

              ExposedDropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false })
              {
                itemsStatus.forEach { item ->
                  DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                      selectedStatus = item
                      expandedStatus = false
                      viewModel.updateStatusFilter(item)
                    }
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Customers List
      if (customers.isEmpty()) {
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
          items(customers) { customer ->
            CustomerCard(
              customer = customer,
              onClick = {
                selectedCustomer = customer
                showCustomerDetails = true
              }
            )
          }
        }
      }
    }

    // Bottom Navigation
    ScrollableBottomNavigation(
      onClientsClick = { /* Already on clients screen */ },
      onChatsClick = onChatsClick,
      onInvitesClick = onInvitesClick,
      onPromotionsClick = onPromotionsClick,
      onCampaignsClick = onCampaignsClick,
      onBannersClick = onBannersClick,
      onLogoutClick = onLogoutClick,
      isClientsActive = true,
      isChatsActive = false,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }

  // Customer Details Modal
  if (showCustomerDetails && selectedCustomer != null) {
    CustomerDetailsModal(
      customer = selectedCustomer!!,
      onDismiss = {
        showCustomerDetails = false
        selectedCustomer = null
      },
      onSendMessage = {
        showCustomerDetails = false
        onCustomerClick(selectedCustomer!!)
      },
      onSaveNotes = { notes ->
        viewModel.saveNotes(selectedCustomer!!.id, notes)
        showCustomerDetails = false
        selectedCustomer = null
      }
    )
  }
}

@Composable
fun CustomerCard(
  customer: User,
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
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(slate200),
        contentAlignment = Alignment.Center
      ) {
        Icon(Icons.Default.Person, contentDescription = "Criar Convite")
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = customer.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = slate800
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = customer.email,
          fontSize = 14.sp,
          color = slate600
        )
      }

      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "View Details",
        tint = slate400,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
fun ScrollableBottomNavigation(
  onClientsClick: () -> Unit,
  onChatsClick: () -> Unit,
  onInvitesClick: () -> Unit,
  onPromotionsClick: () -> Unit,
  onCampaignsClick: () -> Unit,
  onBannersClick: () -> Unit,
  onLogoutClick: () -> Unit,
  isClientsActive: Boolean = false,
  isChatsActive: Boolean = false,
  isInvitesActive: Boolean = false,
  isPromotionsActive: Boolean = false,
  isCampaignsActive: Boolean = false,
  isBannersActive: Boolean = false,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    colors = CardDefaults.cardColors(containerColor = slate800)
  ) {
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.Start
    ) {
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onClientsClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Clientes",
            tint = if (isClientsActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Clientes",
            color = if (isClientsActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onChatsClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = "Chats",
            tint = if (isChatsActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Chats",
            color = if (isChatsActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onInvitesClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = "Convites",
            tint = if (isInvitesActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Convites",
            color = if (isInvitesActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onPromotionsClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CardGiftcard,
            contentDescription = "Promoções",
            tint = if (isPromotionsActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Promoções",
            color = if (isPromotionsActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onCampaignsClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = "Campanhas",
            tint = if (isCampaignsActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Campanhas",
            color = if (isCampaignsActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onBannersClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PhotoCameraBack,
            contentDescription = "Banners",
            tint = if (isBannersActive) purple500 else slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Banners",
            color = if (isBannersActive) purple500 else slate400,
            fontSize = 12.sp
          )
        }
      }
      item {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clickable { onLogoutClick() }
            .width(80.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Sair",
            tint = slate400,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Sair",
            color = slate400,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}
