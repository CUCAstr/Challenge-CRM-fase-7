package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.viewmodel.AuthUIState
import br.com.savedra.challengecrm.viewmodel.AuthViewModel

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authUiState.collectAsState()

    // Inicia a verificação de autenticação assim que a tela é composta
    LaunchedEffect(Unit) {
        authViewModel.performInitialAuthCheck()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    // A lógica de navegação existente reagirá ao resultado do `authState`
    when (val state = authState) {
        is AuthUIState.Success -> {
            // Navega para a tela principal apropriada se o login for bem-sucedido
            LaunchedEffect(state) {
                val route = when (state.user?.role) {
                    "Cliente" -> AppRoutes.CLIENT_HOME
                    "Operador" -> AppRoutes.OPERATOR_HOME
                    else -> AppRoutes.LANDING // Fallback para landing se o usuário não tiver role
                }
                navController.navigate(route) {
                    popUpTo(AppRoutes.SPLASH) { inclusive = true }
                }
            }
        }
        is AuthUIState.Error, AuthUIState.Idle -> {
            // Navega para a tela de boas-vindas se não houver usuário logado ou ocorrer um erro
            LaunchedEffect(state) {
                navController.navigate(AppRoutes.LANDING) {
                    popUpTo(AppRoutes.SPLASH) { inclusive = true }
                }
            }
        }
        is AuthUIState.Loading -> {
            // O CircularProgressIndicator já está sendo exibido
        }
    }
}
