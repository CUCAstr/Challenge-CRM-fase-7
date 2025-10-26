package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.model.User
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.CustomerViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatorHomeScreen(
    onCustomerClick: (User) -> Unit = {},
    onCampaignsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    viewModel: CustomerViewModel = viewModel()
) {
    val customers by viewModel.filteredCustomers.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showCustomerDetails by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<User?>(null) }

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

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    var expandedVip by remember { mutableStateOf(false) }
                    val itemsVip = listOf("Todos", "VIP", "Não VIP")
                    var selectedVip by remember { mutableStateOf(itemsVip[0]) }

                    ExposedDropdownMenuBox(
                        expanded = expandedVip,
                        onExpandedChange = { expandedVip = !expandedVip },
                        modifier = Modifier.weight(1f)
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
                                Text(text = selectedVip, modifier = Modifier.padding(horizontal = 8.dp))
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVip)
                            }
                        }

                        ExposedDropdownMenu(
                            expanded = expandedVip,
                            onDismissRequest = { expandedVip = false })
                        {
                            itemsVip.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedVip = item
                                        expandedVip = false
                                        viewModel.updateVipFilter(item)
                                    }
                                )
                            }
                        }
                    }

                    var expandedState by remember { mutableStateOf(false) }
                    val itemsState = listOf(
                        "Todos", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
                        "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
                    )
                    var selectedState by remember { mutableStateOf(itemsState[0]) }

                    ExposedDropdownMenuBox(
                        expanded = expandedState,
                        onExpandedChange = { expandedState = !expandedState },
                        modifier = Modifier.weight(1f)
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
                                Text(text = selectedState, modifier = Modifier.padding(horizontal = 8.dp))
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)
                            }
                        }

                        ExposedDropdownMenu(
                            expanded = expandedState,
                            onDismissRequest = { expandedState = false })
                        {
                            itemsState.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedState = item
                                        expandedState = false
                                        viewModel.updateStateFilter(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Customers List
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

        // Bottom Navigation
        BottomNavigation(
            onClientsClick = { /* Already on clients screen */ },
            onCampaignsClick = onCampaignsClick,
            onLogoutClick = onLogoutClick,
            isClientsActive = true,
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
            onSendMessage = { onCustomerClick(selectedCustomer!!) }
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
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(slate200),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = customer.name.first().toString().uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate600
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Customer Info
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

            // Chevron
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
fun BottomNavigation(
    onClientsClick: () -> Unit,
    onCampaignsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    isClientsActive: Boolean = false,
    isCampaignsActive: Boolean = false,
    modifier: Modifier = Modifier
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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Clients
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onClientsClick() }
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

            // Campaigns
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCampaignsClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Operações",
                    tint = if (isCampaignsActive) purple500 else slate400,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Operações",
                    color = if (isCampaignsActive) purple500 else slate400,
                    fontSize = 12.sp
                )
            }

            // Logout
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onLogoutClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
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
