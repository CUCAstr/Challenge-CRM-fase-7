package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.ui.theme.white
import br.com.savedra.challengecrm.viewmodel.OperationsViewModel
import androidx.compose.foundation.lazy.LazyColumn
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationsScreen(
    viewModel: OperationsViewModel = viewModel(),
    onClientsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    var selectedOperation by remember { mutableStateOf(OperationType.NONE) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Operações", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = slate50,
                    titleContentColor = slate800
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                onClientsClick = onClientsClick,
                onCampaignsClick = {},
                onLogoutClick = onLogoutClick,
                isCampaignsActive = true
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(slate50)
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { selectedOperation = OperationType.INVITE }) {
                        Text("Convite")
                    }
                    Button(onClick = { selectedOperation = OperationType.CAMPAIGN }) {
                        Text("Campanha")
                    }
                    Button(onClick = { selectedOperation = OperationType.MESSAGE }) {
                        Text("Mensagem")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                when (selectedOperation) {
                    OperationType.INVITE -> InviteForm(viewModel)
                    OperationType.CAMPAIGN -> CampaignForm(viewModel)
                    OperationType.MESSAGE -> MessageForm(viewModel)
                    OperationType.NONE -> {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Selecione uma operação",
                                fontSize = 18.sp,
                                color = slate800
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InviteForm(viewModel: OperationsViewModel) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val context = LocalContext.current
    val startDateString by viewModel.startDateString.collectAsState()
    val todayInMillis = remember {
        Instant.now().atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay()
            .toInstant(ZoneId.of("UTC").rules.getOffset(Instant.now()))
            .toEpochMilli()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            DatePickerField(
                label = "Data de Início",
                dateString = startDateString,
                onDateSelected = { millis ->
                    viewModel.onStartDateSelected(millis)
                },
                dateValidator = { millis ->
                    millis >= todayInMillis
                }
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Local") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            SegmentFilters(viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.sendInvite(name, description, startDateString, location)
                    Toast.makeText(context, "Convite enviado com sucesso!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Convite")
            }
        }
    }
}

@Composable
fun CampaignForm(viewModel: OperationsViewModel) {
    val textCampaignToast = "Sua campanha foi enviada com sucesso!"
    val durationCampaignToast = Toast.LENGTH_SHORT
    val toastCompaign = Toast.makeText(
        LocalContext.current,
        textCampaignToast,
        durationCampaignToast
    )

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val startDateString by viewModel.startDateString.collectAsState()
    val startDateMillis by viewModel.startDateMillis.collectAsState()
    val endDateString by viewModel.endDateString.collectAsState()
    val todayInMillis = remember {
        Instant.now().atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay()
            .toInstant(ZoneId.of("UTC").rules.getOffset(Instant.now()))
            .toEpochMilli()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome da Campanha") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            DatePickerField(
                label = "Data de Início",
                dateString = startDateString,
                onDateSelected = { millis ->
                    viewModel.onStartDateSelected(millis)
                },
                dateValidator = { millis ->
                    millis >= todayInMillis
                }
            )
            DatePickerField(
                label = "Data de Fim",
                dateString = endDateString,
                onDateSelected = { millis ->
                    viewModel.onEndDateSelected(millis)
                },
                dateValidator = { millisToValidate ->

                    val isAfterToday = millisToValidate >= todayInMillis

                    val isAfterStartDate = startDateMillis?.let { start ->
                        millisToValidate >= start
                    } ?: true

                    isAfterToday && isAfterStartDate
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SegmentFilters(viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.sendCampaign(name, description, startDateString, endDateString); toastCompaign.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Campanha")
            }
        }
    }
}

@Composable
fun MessageForm(viewModel: OperationsViewModel) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensagem") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            SegmentFilters(viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.sendMessage(title, message)
                    Toast.makeText(context, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Mensagem")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentFilters(
    viewModel: OperationsViewModel,
    modifier: Modifier = Modifier
) {
    val vipFilter by viewModel.vipFilter.collectAsState()
    val stateFilter by viewModel.stateFilter.collectAsState()

    Column(modifier = modifier) {
        Text(
            text = "Segmentação",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = slate800
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
}

enum class OperationType {
    NONE,
    INVITE,
    CAMPAIGN,
    MESSAGE
}