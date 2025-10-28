package br.com.savedra.challengecrm.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.savedra.challengecrm.ui.view.BannersScreen
import br.com.savedra.challengecrm.ui.view.CampaignsScreen
import br.com.savedra.challengecrm.ui.view.ChatScreen
import br.com.savedra.challengecrm.ui.view.ClientHomeScreen
import br.com.savedra.challengecrm.ui.view.InvitesScreen
import br.com.savedra.challengecrm.ui.view.LoginScreen
import br.com.savedra.challengecrm.ui.view.OperatorHomeScreen
import br.com.savedra.challengecrm.ui.view.PromotionsScreen
import br.com.savedra.challengecrm.ui.view.RegisterScreen
import br.com.savedra.challengecrm.viewmodel.CustomerViewModel

object AppRoutes {
  const val LOGIN = "login"
  const val REGISTER = "register"
  const val CLIENT_HOME = "clientHome"
  const val OPERATOR_HOME = "operatorHome"
  const val CHAT = "chat"
  const val INVITES = "invites"
  const val PROMOTIONS = "promotions"
  const val CAMPAIGNS = "campaigns"
  const val BANNERS = "banners"
}

@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  val customerViewModel: CustomerViewModel = viewModel()

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
        viewModel = customerViewModel,
        onCustomerClick = { customer ->
          navController.navigate("${AppRoutes.CHAT}/${customer.id}/${customer.name}")
        },
        onInvitesClick = {
          navController.navigate(AppRoutes.INVITES)
        },
        onPromotionsClick = {
          navController.navigate(AppRoutes.PROMOTIONS)
        },
        onCampaignsClick = {
          navController.navigate(AppRoutes.CAMPAIGNS)
        },
        onBannersClick = {
          navController.navigate(AppRoutes.BANNERS)
        },
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        }
      )
    }
    composable(AppRoutes.INVITES) {
      InvitesScreen(
        onClientsClick = {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true }
          }
        },
        onPromotionsClick = {
          navController.navigate(AppRoutes.PROMOTIONS)
        },
        onCampaignsClick = {
          navController.navigate(AppRoutes.CAMPAIGNS)
        },
        onBannersClick = {
          navController.navigate(AppRoutes.BANNERS)
        },
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        }
      )
    }
    composable(AppRoutes.PROMOTIONS) {
      PromotionsScreen(
        onClientsClick = {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true }
          }
        },
        onInvitesClick = {
          navController.navigate(AppRoutes.INVITES)
        },
        onCampaignsClick = {
          navController.navigate(AppRoutes.CAMPAIGNS)
        },
        onBannersClick = {
          navController.navigate(AppRoutes.BANNERS)
        },
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        }
      )
    }
    composable(AppRoutes.CAMPAIGNS) {
      CampaignsScreen(
        onClientsClick = {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true }
          }
        },
        onInvitesClick = {
          navController.navigate(AppRoutes.INVITES)
        },
        onPromotionsClick = {
          navController.navigate(AppRoutes.PROMOTIONS)
        },
        onBannersClick = {
          navController.navigate(AppRoutes.BANNERS)
        },
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        }
      )
    }
    composable(AppRoutes.BANNERS) {
      BannersScreen(
        onClientsClick = {
          navController.navigate(AppRoutes.OPERATOR_HOME) {
            popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true }
          }
        },
        onInvitesClick = {
          navController.navigate(AppRoutes.INVITES)
        },
        onPromotionsClick = {
          navController.navigate(AppRoutes.PROMOTIONS)
        },
        onCampaignsClick = {
          navController.navigate(AppRoutes.CAMPAIGNS)
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
