package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import br.com.savedra.challengecrm.model.Banner
import br.com.savedra.challengecrm.ui.theme.indigo500

/**
 * Modal de Detalhes de um Banner.
 */
@Composable
fun BannerDetailsModal(banner: Banner, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(banner.title ?: "", fontWeight = FontWeight.Bold) },
        text = { Text(banner.description ?: "") },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Fechar", color = indigo500) }
        }
    )
}
