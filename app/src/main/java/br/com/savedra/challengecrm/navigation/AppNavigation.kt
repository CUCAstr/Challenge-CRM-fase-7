package br.com.savedra.challengecrm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.savedra.challengecrm.ui.view.ChatScreen
import br.com.savedra.challengecrm.ui.view.ClientHomeScreen
import br.com.savedra.challengecrm.ui.view.LoginScreen
import br.com.savedra.challengecrm.ui.view.OperatorHomeScreen
import br.com.savedra.challengecrm.ui.view.RegisterScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CLIENT_HOME = "clientHome"
    const val OPERATOR_HOME = "operatorHome"
    const val CHAT = "chat"
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
        composable(AppRoutes.CLIENT_HOME) {
            ClientHomeScreen(
                onMessageClick = { message ->
                    if (message.type == br.com.savedra.challengecrm.model.MessageType.MESSAGE) {
                        navController.navigate(AppRoutes.CHAT)
                    }
                },
                onLogoutClick = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.OPERATOR_HOME) {
            OperatorHomeScreen(
                onCampaignsClick = {
                    // TODO: Navigate to campaigns screen
                },
                onLogoutClick = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.CHAT) {
            ChatScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
