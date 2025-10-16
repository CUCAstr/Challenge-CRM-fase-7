package br.com.savedra.challengecrm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.savedra.challengecrm.ui.view.LoginScreen
import br.com.savedra.challengecrm.ui.view.RegisterScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(AppRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }
    }
}
