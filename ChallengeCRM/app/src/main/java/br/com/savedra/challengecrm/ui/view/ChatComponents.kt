package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.model.Message
import br.com.savedra.challengecrm.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Bolha de mensagem reutilizável.
 */
@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
  val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
  val backgroundColor = if (isCurrentUser) indigo500 else white
  val textColor = if (isCurrentUser) white else slate800
  val shape = if (isCurrentUser) {
    RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
  } else {
    RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
  }

  Column(
    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    horizontalAlignment = alignment
  ) {
    Surface(
      color = backgroundColor,
      shape = shape,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(text = message.text ?: "", color = textColor, fontSize = 16.sp)
        Text(
          text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.timestamp ?: Date()),
          color = if (isCurrentUser) indigo100 else slate400,
          fontSize = 10.sp,
          modifier = Modifier.align(Alignment.End)
        )
      }
    }
  }
}

/**
 * Linha de entrada de mensagem reutilizável.
 * 
 * CORREÇÕES:
 * 1. Aplicado navigationBarsPadding() para evitar que os botões do Android fiquem em cima do input.
 * 2. Aplicado imePadding() para que a caixa de texto suba junto com o teclado.
 */
@Composable
fun MessageInputRow(
  text: String,
  onTextChange: (String) -> Unit,
  onSendClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding() // CORREÇÃO: Respeita os botões de navegação do sistema
      .imePadding(),          // CORREÇÃO: Respeita a subida do teclado
    color = white,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier.padding(16.dp).fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text("Digite sua mensagem...") },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200,
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedContainerColor = slate50,
          unfocusedContainerColor = slate50
        )
      )
      Spacer(modifier = Modifier.width(8.dp))
      IconButton(
        onClick = onSendClick,
        colors = IconButtonDefaults.iconButtonColors(containerColor = indigo500, contentColor = white)
      ) {
        Icon(Icons.Default.Send, contentDescription = "Enviar")
      }
    }
  }
}
