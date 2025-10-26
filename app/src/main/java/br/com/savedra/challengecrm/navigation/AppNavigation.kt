package br.com.savedra.challengecrm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.savedra.challengecrm.ui.view.ChatScreen
import br.com.savedra.challengecrm.ui.view.ClientHomeScreen
import br.com.savedra.challengecrm.ui.view.LoginScreen
import br.com.savedra.challengecrm.ui.view.OperatorHomeScreen
import br.com.savedra.challengecrm.ui.view.RegisterScreen
import br.com.savedra.challengecrm.ui.view.OperationsScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CLIENT_HOME = "clientHome"
    const val OPERATOR_HOME = "operatorHome"
    const val CHAT = "chat"
    const val OPERATIONS = "operations"
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
                onMessageClick = { 
                    navController.navigate("${AppRoutes.CHAT}/operadorId/Operador")
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
                onCustomerClick = { customer ->
                    navController.navigate("${AppRoutes.CHAT}/${customer.id}/${customer.name}")
                },
                onCampaignsClick = {
                    navController.navigate(AppRoutes.OPERATIONS)
                },
                onLogoutClick = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.OPERATIONS) {
            OperationsScreen(
                onClientsClick = {
                    navController.navigate(AppRoutes.OPERATOR_HOME) {
                        popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true }
                    }
                },
                onLogoutClick = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "${AppRoutes.CHAT}/{userId}/{userName}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            ChatScreen(
                userId = userId,
                userName = userName,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
