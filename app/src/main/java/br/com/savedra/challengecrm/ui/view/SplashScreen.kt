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

    LaunchedEffect(Unit) {
        authViewModel.checkCurrentUser()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    when (val state = authState) {
        is AuthUIState.Success -> {
            LaunchedEffect(state) {
                val route = when (state.role) {
                    "Cliente" -> AppRoutes.CLIENT_HOME
                    "Operador" -> AppRoutes.OPERATOR_HOME
                    else -> AppRoutes.LANDING
                }
                navController.navigate(route) {
                    popUpTo(AppRoutes.SPLASH) { inclusive = true }
                }
            }
        }
        is AuthUIState.Error -> {
            LaunchedEffect(state) {
                navController.navigate(AppRoutes.LANDING) {
                    popUpTo(AppRoutes.SPLASH) { inclusive = true }
                }
            }
        }
        else -> {}
    }
}
