package br.com.savedra.challengecrm.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.ui.theme.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Campo de texto padrão com suporte a TAB físico e alto contraste.
 */
@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = slate700) },
        modifier = modifier
            .fillMaxWidth()
            .onPreviewKeyEvent { keyEvent ->
                // CORREÇÃO: Trata a tecla TAB física para mover o foco em vez de inserir caractere
                if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                    focusManager.moveFocus(FocusDirection.Next)
                    true
                } else {
                    false
                }
            },
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(
            imeAction = if (keyboardOptions.imeAction == ImeAction.Default) ImeAction.Next else keyboardOptions.imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = indigo500,
            unfocusedBorderColor = slate300
        )
    )
}

/**
 * Campo de Data Uniforme (Manual + Ícone).
 */
@Composable
fun StandardDateInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    visualTransformation: VisualTransformation
) {
    StandardTextField(
        value = value,
        onValueChange = { if (it.length <= 8 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = onIconClick) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Abrir calendário", tint = indigo500)
            }
        }
    )
}

/**
 * Campo de Hora Uniforme (Manual + Ícone).
 */
@Composable
fun StandardTimeInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onIconClick: () -> Unit,
    visualTransformation: VisualTransformation
) {
    StandardTextField(
        value = value,
        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) onValueChange(it) },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = onIconClick) {
                Icon(Icons.Default.AccessTime, contentDescription = "Abrir relógio", tint = indigo500)
            }
        }
    )
}
