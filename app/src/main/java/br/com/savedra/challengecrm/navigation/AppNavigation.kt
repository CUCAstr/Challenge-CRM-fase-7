package br.com.savedra.challengecrm.navigation

import ChatScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.savedra.challengecrm.ui.view.*
import br.com.savedra.challengecrm.viewmodel.ChatViewModel
import br.com.savedra.challengecrm.viewmodel.OperatorViewModel
import androidx.compose.runtime.*

import br.com.savedra.challengecrm.viewmodel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth

object AppRoutes {
  const val LANDING = "landing"
  const val LOGIN = "login"
  const val REGISTER = "register"
  const val CLIENT_HOME = "clientHome"
  const val OPERATOR_HOME = "operatorHome"
  const val CHAT = "chat"
  const val INVITES = "invites"
  const val PROMOTIONS = "promotions"
  const val CAMPAIGNS = "campaigns"
  const val BANNERS = "banners"
  const val EVENTS_CENTER = "eventsCenter"
  const val BUSINESS_CLUB = "businessClub"
  const val SHERATON_HOTEL = "sheratonHotel"
  const val OPERATOR_LIST = "operatorList"
}

@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  val customerViewModel: OperatorViewModel = viewModel()
  val usersViewModel: UsersViewModel = viewModel()

  NavHost(navController = navController, startDestination = AppRoutes.LANDING) {
    composable(AppRoutes.LANDING) {
      LandingScreen(onAccessSystemClick = {
        navController.navigate(AppRoutes.LOGIN)
      })
    }
    composable(AppRoutes.LOGIN) {
      LoginScreen(navController = navController)
    }
    composable(AppRoutes.REGISTER) {
      RegisterScreen(navController = navController)
    }
    composable(AppRoutes.CLIENT_HOME) {
      ClientHomeScreen(
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        },
        onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) },
        onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) },
        onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) },
        onChatClick = {
          navController.navigate(AppRoutes.OPERATOR_LIST)
        }
      )
    }
    composable(AppRoutes.OPERATOR_HOME) {
      OperatorHomeScreen(
        viewModel = customerViewModel,
        onCustomerClick = {
          val currentUser = FirebaseAuth.getInstance().currentUser
          navController.navigate("${AppRoutes.CHAT}/${currentUser?.uid}/${it.id}")
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
    composable(
      route = "${AppRoutes.CHAT}/{operatorId}/{userId}",
      arguments = listOf(
        navArgument("operatorId") { type = NavType.StringType },
        navArgument("userId") { type = NavType.StringType }
      )
    ) { backStackEntry ->
      val operatorId = backStackEntry.arguments?.getString("operatorId")
      val userId = backStackEntry.arguments?.getString("userId")
      val chatViewModel: ChatViewModel = viewModel()
      val users by usersViewModel.users.collectAsState()

      val operator = users.find { it.id == operatorId }
      val user = users.find { it.id == userId }
      val currentSenderId = FirebaseAuth.getInstance().currentUser?.uid
      val currentUser = users.find { it.id == currentSenderId }
      val currentUserRole = currentUser?.role

      if (operator != null && user != null && currentSenderId != null && currentUserRole != null) {
        ChatScreen(
          viewModel = chatViewModel,
          operator = operator,
          user = user,
          currentSenderId = currentSenderId,
          currentUserRole = currentUserRole
        )
      } else {
        // Handle user not found
      }
    }
    composable(AppRoutes.OPERATOR_LIST) {
      OperatorListScreen(
        usersViewModel = usersViewModel,
        onOperatorClick = { operator ->
          val currentUser = FirebaseAuth.getInstance().currentUser
          navController.navigate("${AppRoutes.CHAT}/${operator.id}/${currentUser?.uid}")
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
    composable(AppRoutes.EVENTS_CENTER) {
      EventsCenterScreen(
        navController = navController,
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        },
        onInboxClick = {
          navController.navigate(AppRoutes.CLIENT_HOME)
        },
        onBusinessClubClick = {
          navController.navigate(AppRoutes.BUSINESS_CLUB)
        },
        onSheratonHotelClick = {
          navController.navigate(AppRoutes.SHERATON_HOTEL)
        }
      )
    }
    composable(AppRoutes.BUSINESS_CLUB) {
      BusinessClubScreen(
        navController = navController,
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        },
        onInboxClick = {
          navController.navigate(AppRoutes.CLIENT_HOME)
        },
        onEventsCenterClick = {
          navController.navigate(AppRoutes.EVENTS_CENTER)
        },
        onSheratonHotelClick = {
          navController.navigate(AppRoutes.SHERATON_HOTEL)
        }
      )
    }
    composable(AppRoutes.SHERATON_HOTEL) {
      SheratonHotelScreen(
        navController = navController,
        onLogoutClick = {
          navController.navigate(AppRoutes.LOGIN) {
            popUpTo(AppRoutes.LOGIN) { inclusive = true }
          }
        },
        onInboxClick = {
          navController.navigate(AppRoutes.CLIENT_HOME)
        },
        onEventsCenterClick = {
          navController.navigate(AppRoutes.EVENTS_CENTER)
        },
        onBusinessClubClick = {
          navController.navigate(AppRoutes.BUSINESS_CLUB)
        }
      )
    }
  }
}
