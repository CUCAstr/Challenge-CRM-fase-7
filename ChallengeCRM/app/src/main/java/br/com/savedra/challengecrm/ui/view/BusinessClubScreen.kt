package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.BusinessClub
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.*
import br.com.savedra.challengecrm.viewmodel.BusinessClubUIState
import br.com.savedra.challengecrm.viewmodel.BusinessClubViewModel

/**
 * Tela de Solicitação de Orçamento para Business Club.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessClubScreen(
  navController: NavController,
  onLogoutClick: () -> Unit,
  onInboxClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  viewModel: BusinessClubViewModel = viewModel()
) {
  var reason by remember { mutableStateOf("") }
  var internalPartnersContact by remember { mutableStateOf(false) }

  val uiState by viewModel.businessUiState.collectAsState()
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current

  LaunchedEffect(uiState) {
    if (uiState is BusinessClubUIState.Success) {
      Toast.makeText(context, "Solicitação enviada com sucesso!", Toast.LENGTH_LONG).show()
      navController.navigate(AppRoutes.CLIENT_HOME) { popUpTo(AppRoutes.CLIENT_HOME) { this.inclusive = true } }
      viewModel.resetUiState()
    }
  }

  Scaffold(
    modifier = Modifier.systemBarsPadding(),
    containerColor = slate50,
    topBar = {
        TopAppBar(
            title = { Text("Business Club", color = slate800, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = slate800)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = white)
        )
    },
    bottomBar = {
        BottomNavigationClient(
            onInboxClick = onInboxClick,
            onLogoutClick = onLogoutClick,
            isInboxActive = false,
            isEventsCenterActive = false,
            isBusinessClubActive = true,
            isSheratonHotelActive = false,
            onEventsCenterClick = onEventsCenterClick,
            onBusinessClubClick = { },
            onSheratonHotelClick = onSheratonHotelClick
        )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(24.dp)
    ) {
      Text(
        text = "FAÇA PARTE DO CLUBE",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = slate800,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = reason,
        onValueChange = { reason = it },
        label = { Text("Motivo do contato *", color = slate700) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
            focusedContainerColor = white, unfocusedContainerColor = white
        )
      )
      Spacer(modifier = Modifier.height(16.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
          Checkbox(
              checked = internalPartnersContact,
              onCheckedChange = { internalPartnersContact = it }
          )
          Text("Desejo receber contato de parceiros", color = slate800)
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          val bc = BusinessClub(reason = reason, internalPartnersContact = internalPartnersContact)
          viewModel.saveBusinessClub(bc)
        },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = slate800)
      ) {
        if (uiState is BusinessClubUIState.Loading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = white)
        } else {
            Text("SOLICITAR CONTATO", fontWeight = FontWeight.Bold)
        }
      }
      
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}
