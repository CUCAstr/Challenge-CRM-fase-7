package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*

/**
 * Tela de Seleção de Segmentos para Chat de Grupo.
 * 
 * CORREÇÕES:
 * 1. Adicionado Scaffold com TopAppBar (Botão Voltar).
 * 2. Adicionado ScrollableBottomNavigation para manter o menu visível.
 * 3. Aplicado systemBarsPadding para evitar sobreposição com a câmera.
 * 4. Alto contraste nos textos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentChatsScreen(
    navController: NavController,
    onClientsClick: () -> Unit = {},
    onInvitesClick: () -> Unit = {},
    onPromotionsClick: () -> Unit = {},
    onCampaignsClick: () -> Unit = {},
    onBannersClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val segments = listOf(
        "ED", "IT", "Retail & Financial", "GRC", "HR", "Smart Spends", "Health", "CSC", "Field Marketing", "Finance", "ESG", "CX"
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = slate50,
        topBar = {
            TopAppBar(
                title = { Text("Chats por Segmento", color = slate800, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
            )
        },
        bottomBar = {
            ScrollableBottomNavigation(
                onClientsClick = onClientsClick,
                onInvitesClick = onInvitesClick,
                onPromotionsClick = onPromotionsClick,
                onCampaignsClick = onCampaignsClick,
                onBannersClick = onBannersClick,
                onChatsClick = { }, // Já estamos na aba de chats
                onLogoutClick = onLogoutClick,
                isChatsActive = true
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
                text = "Escolha um segmento para ver o histórico de mensagens.",
                fontSize = 14.sp,
                color = slate600,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(segments) { segment ->
                    SegmentChatCard(segment = segment) {
                        navController.navigate("${AppRoutes.GROUP_CHAT}/$segment")
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentChatCard(
    segment: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = segment,
                style = MaterialTheme.typography.bodyLarge,
                color = slate800,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir chat",
                tint = slate600
            )
        }
    }
}
