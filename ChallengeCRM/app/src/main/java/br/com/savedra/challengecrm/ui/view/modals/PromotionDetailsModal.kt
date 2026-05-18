package br.com.savedra.challengecrm.ui.view.modals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.com.savedra.challengecrm.model.Promotion
import br.com.savedra.challengecrm.ui.theme.*

import br.com.savedra.challengecrm.util.FormatUtils

/**
 * Modal de Detalhes da Promoção.
 */
@Composable
fun PromotionDetailsModal(
    promotion: Promotion,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = slate50)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = promotion.title ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = slate800
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = promotion.description ?: "", fontSize = 16.sp, color = slate700)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row {
                    Text(text = "De: ", color = slate600)
                    Text(text = "R$ ${promotion.originalValue ?: ""}", style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Por: R$ ${promotion.promotionValue ?: ""}", color = green500, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                // CORREÇÃO: Formatação amigável
                val dateF = FormatUtils.formatDate(promotion.dateExpiresIn ?: "")
                val timeF = FormatUtils.formatTime(promotion.hoursExpiresIn ?: "")
                Text(text = "Expira em: $dateF às $timeF", fontSize = 14.sp, color = slate600)
                Text(text = "Segmento: ${promotion.segment ?: ""}", fontSize = 14.sp, color = slate600)

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = indigo500)
                    ) {
                        Text("Fechar")
                    }
                }
            }
        }
    }
}
