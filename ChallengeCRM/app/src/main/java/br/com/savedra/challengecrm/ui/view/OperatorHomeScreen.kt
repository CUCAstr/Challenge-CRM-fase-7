package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCameraBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.OperatorViewModel
import br.com.savedra.challengecrm.ui.view.modals.CustomerDetailsModal
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.lazy.LazyRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorHomeScreen(
  navController: NavController,
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
  var showCreateOperator by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    viewModel.loadCustomers()
    viewModel.updateSegmentFilter("Todos")
    viewModel.updateStatusFilter("Todos")
    focusManager.clearFocus()
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    floatingActionButton = {
        FloatingActionButton(
            onClick = { showCreateOperator = true },
            containerColor = indigo500,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Novo Operador")
        }
    },
    bottomBar = {
      ScrollableBottomNavigation(
        navController = navController,
        onClientsClick = { },
        onChatsClick = onChatsClick,
        onInvitesClick = onInvitesClick,
        onPromotionsClick = onPromotionsClick,
        onCampaignsClick = onCampaignsClick,
        onBannersClick = onBannersClick,
        onLogoutClick = onLogoutClick,
        isClientsActive = true
      )
    }
  ) { innerPadding ->
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
      Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
          Text(text = "Painel do Operador", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = slate800)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Gestão de Clientes", fontSize = 16.sp, color = slate600)
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = slate400, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(12.dp))
              TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Filtrar por nome...", color = slate400) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = TextFieldDefaults.colors(
                  focusedContainerColor = Color.Transparent,
                  unfocusedContainerColor = Color.Transparent,
                  focusedIndicatorColor = Color.Transparent,
                  unfocusedIndicatorColor = Color.Transparent,
                  focusedTextColor = Color.Black,
                  unfocusedTextColor = Color.Black
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
              Text("Segmento", color = slate600, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              var expandedSegment by remember { mutableStateOf(false) }
              val itemsSegment = listOf("Todos", "ED", "IT", "Retail & Financial", "GRC", "HR", "Smart Spends", "Health", "CSC", "Field Marketing", "Finance", "ESG", "CX")
              var selectedSegment by remember { mutableStateOf(itemsSegment[0]) }

              ExposedDropdownMenuBox(expanded = expandedSegment, onExpandedChange = { expandedSegment = !expandedSegment }) {
                OutlinedTextField(
                  value = selectedSegment,
                  onValueChange = {},
                  readOnly = true,
                  modifier = Modifier.menuAnchor().fillMaxWidth(),
                  trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSegment) },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = white, unfocusedContainerColor = white,
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = indigo500, unfocusedBorderColor = slate300
                  )
                )
                ExposedDropdownMenu(expanded = expandedSegment, onDismissRequest = { expandedSegment = false }, modifier = Modifier.background(white)) {
                  itemsSegment.forEach { item ->
                    DropdownMenuItem(
                      text = { Text(item, color = Color.Black) },
                      onClick = { selectedSegment = item; expandedSegment = false; viewModel.updateSegmentFilter(item) },
                      modifier = Modifier.background(white)
                    )
                  }
                }
              }
            }

            Column(modifier = Modifier.weight(1f)) {
              Text("Status", color = slate600, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              var expandedStatus by remember { mutableStateOf(false) }
              val itemsStatus = listOf("Todos", "Ativo", "Inativo")
              var selectedStatus by remember { mutableStateOf(itemsStatus[0]) }

              ExposedDropdownMenuBox(expanded = expandedStatus, onExpandedChange = { expandedStatus = !expandedStatus }) {
                OutlinedTextField(
                  value = selectedStatus,
                  onValueChange = {},
                  readOnly = true,
                  modifier = Modifier.menuAnchor().fillMaxWidth(),
                  trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = white, unfocusedContainerColor = white,
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = indigo500, unfocusedBorderColor = slate300
                  )
                )
                ExposedDropdownMenu(expanded = expandedStatus, onDismissRequest = { expandedStatus = false }, modifier = Modifier.background(white)) {
                  itemsStatus.forEach { item ->
                    DropdownMenuItem(
                      text = { Text(item, color = Color.Black) },
                      onClick = { selectedStatus = item; expandedStatus = false; viewModel.updateStatusFilter(item) },
                      modifier = Modifier.background(white)
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (customers.isEmpty()) {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhum cliente encontrado.", color = slate800, fontWeight = FontWeight.Medium)
          }
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
          ) {
            items(customers) { customer ->
              CustomerCard(customer = customer, onClick = { selectedCustomer = customer; showCustomerDetails = true })
            }
          }
        }
      }
    }
  }

  if (showCustomerDetails && selectedCustomer != null) {
    CustomerDetailsModal(
      customer = selectedCustomer!!,
      onDismiss = { showCustomerDetails = false; selectedCustomer = null },
      onSendMessage = { showCustomerDetails = false; onCustomerClick(selectedCustomer!!) },
      onSaveNotes = { notes ->
        viewModel.saveNotes(selectedCustomer?.id ?: "", notes)
        showCustomerDetails = false
        selectedCustomer = null
      }
    )
  }

  if (showCreateOperator) {
    br.com.savedra.challengecrm.ui.view.modals.CreateOperatorModal(
        onDismiss = { showCreateOperator = false },
        onSuccess = { showCreateOperator = false; viewModel.loadCustomers() }
    )
  }
}

@Composable
fun CustomerCard(customer: User, onClick: () -> Unit) {
  Card(
    modifier = Modifier.fillMaxWidth().clickable { onClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = white),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(slate200), contentAlignment = Alignment.Center) {
        Icon(Icons.Default.Person, contentDescription = null, tint = slate600)
      }
      Spacer(modifier = Modifier.width(16.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(text = customer.name ?: "Sem Nome", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = slate800)
        Text(text = customer.email ?: "Sem Email", fontSize = 14.sp, color = slate600)
      }
      Icon(imageVector = Icons.Default.Person, contentDescription = "Ver Detalhes", tint = slate400, modifier = Modifier.size(20.dp))
    }
  }
}

@Composable
fun ScrollableBottomNavigation(
  navController: NavController,
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
    colors = CardDefaults.cardColors(containerColor = slate800),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
  ) {
    LazyRow(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      item { NavItem(Icons.Default.Person, "Clientes", isClientsActive, onClientsClick) }
      item { NavItem(Icons.Default.Group, "Equipe", false, { navController.navigate(AppRoutes.OPERATOR_LIST) }) }
      item { NavItem(Icons.Default.Mail, "Chats", isChatsActive, onChatsClick) }
      item { NavItem(Icons.Default.Mail, "Convites", isInvitesActive, onInvitesClick) }
      item { NavItem(Icons.Default.CardGiftcard, "Promoções", isPromotionsActive, onPromotionsClick) }
      item { NavItem(Icons.Default.Campaign, "Campanhas", isCampaignsActive, onCampaignsClick) }
      item { NavItem(Icons.Default.PhotoCameraBack, "Banners", isBannersActive, onBannersClick) }
      item { NavItem(Icons.AutoMirrored.Filled.ExitToApp, "Sair", false, onLogoutClick, Color.Red) }
    }
  }
}

@Composable
fun NavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isActive: Boolean, onClick: () -> Unit, tint: Color? = null) {
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.width(80.dp)) {
    Icon(imageVector = icon, contentDescription = label, tint = tint ?: (if (isActive) purple500 else white), modifier = Modifier.size(24.dp))
    Spacer(modifier = Modifier.height(4.dp))
    Text(text = label, color = tint ?: (if (isActive) purple500 else white), fontSize = 11.sp, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
  }
}
