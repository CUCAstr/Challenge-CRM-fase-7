package br.com.savedra.challengecrm.ui.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.model.BusinessClub
import AppRoutes
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.ui.theme.slate600
import br.com.savedra.challengecrm.ui.theme.slate800
import br.com.savedra.challengecrm.viewmodel.BusinessClubViewModel

@Composable
fun BusinessClubScreen(
  navController: NavController,
  onLogoutClick: () -> Unit,
  onInboxClick: () -> Unit,
  onEventsCenterClick: () -> Unit,
  onSheratonHotelClick: () -> Unit,
  businessViewModel: BusinessClubViewModel = viewModel()
) {
  var reason by remember { mutableStateOf("") }
  val internalPartnersContact = remember { mutableStateOf<Boolean?>(null) }

  val businessUiState by businessViewModel.businessUiState.collectAsState()
  val context = LocalContext.current

  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusManager.clearFocus()
  }

  LaunchedEffect(businessUiState) {
    when (businessUiState) {
      is br.com.savedra.challengecrm.viewmodel.BusinessClubUIState.Success -> {
        Toast.makeText(context, "Interesse registrado com sucesso!", Toast.LENGTH_LONG).show()
        navController.navigate(AppRoutes.CLIENT_HOME) {
          popUpTo(AppRoutes.CLIENT_HOME) { inclusive = true }
        }
        businessViewModel.resetUiState()
      }

      else -> {}
    }
  }

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(slate50)
  ) {
    Column(
      modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(24.dp)
    ) {
      Text(
        text = "FORMULÁRIO DE INTERESSE",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = slate800,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Preencha o formulário e explore o ecossistema exclusivo do WTC Business Club.",
        fontSize = 16.sp,
        color = slate600,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = reason,
        onValueChange = { reason = it },
        label = { Text("Porque gostaria de participar? *") },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        "Informamos que sua solicitação será direcionada aos nossos parceiros internos para serviços adicionais. Caso prefira não receber este contato, por favor nos sinalize. *",
        fontSize = 14.sp
      )

      Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
          selected = internalPartnersContact.value == true,
          onClick = { internalPartnersContact.value = true }
        )

        Text("Sim")

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(
          selected = internalPartnersContact.value == false,
          onClick = { internalPartnersContact.value = false }
        )

        Text("Não")
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          val event = BusinessClub(
            reason = reason,
            internalPartnersContact = internalPartnersContact.value
          )
          businessViewModel.saveBusinessClub(event)
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("SOLICITAR ORÇAMENTO")
      }

      when (businessUiState) {
        is br.com.savedra.challengecrm.viewmodel.BusinessClubUIState.Loading -> {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        is br.com.savedra.challengecrm.viewmodel.BusinessClubUIState.Error -> {
          val errorMessage =
            (businessUiState as br.com.savedra.challengecrm.viewmodel.BusinessClubUIState.Error).message
          Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        else -> {}
      }

      Spacer(modifier = Modifier.height(80.dp))
    }

    BottomNavigationClient(
      onInboxClick = onInboxClick,
      onLogoutClick = onLogoutClick,
      isInboxActive = false,
      isEventsCenterActive = false,
      isBusinessClubActive = true,
      isSheratonHotelActive = false,
      modifier = Modifier.align(Alignment.BottomCenter),
      onEventsCenterClick = onEventsCenterClick,
      onBusinessClubClick = { },
      onSheratonHotelClick = onSheratonHotelClick
    )
  }
}