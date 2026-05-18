package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Campaign
import br.com.savedra.challengecrm.ui.theme.indigo500
import br.com.savedra.challengecrm.util.FormatUtils

/**
 * Modal de Detalhes de uma Campanha.
 */
@Composable
fun CampaignDetailsModal(campaign: Campaign, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(campaign.title ?: "", fontWeight = FontWeight.Bold) },
        text = { 
            Column {
                Text(campaign.description ?: "")
                Spacer(Modifier.height(8.dp))
                Text("Início: ${FormatUtils.formatDate(campaign.startDate ?: "")}", fontSize = 12.sp)
                Text("Fim: ${FormatUtils.formatDate(campaign.endDate ?: "")}", fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Fechar", color = indigo500) }
        }
    )
}
