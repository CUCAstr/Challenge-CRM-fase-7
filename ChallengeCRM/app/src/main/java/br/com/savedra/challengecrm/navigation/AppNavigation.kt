package br.com.savedra.challengecrm.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.savedra.challengecrm.ui.view.*
import br.com.savedra.challengecrm.viewmodel.*

/**
 * Gerenciador de Navegação Central.
 */
@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  val customerViewModel: OperatorViewModel = viewModel()
  val usersViewModel: UsersViewModel = viewModel()
  val authViewModel: AuthViewModel = viewModel()
  val currentUser by authViewModel.currentUser.collectAsState()

  NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
    composable(AppRoutes.LANDING) {
      LandingScreen(onAccessSystemClick = { navController.navigate(AppRoutes.LOGIN) })
    }
    composable(AppRoutes.LOGIN) {
      LoginScreen(navController = navController, authViewModel = authViewModel)
    }
    composable(AppRoutes.REGISTER) {
      RegisterScreen(navController = navController, viewModel = authViewModel)
    }
    composable(AppRoutes.CLIENT_HOME) {
      ClientHomeScreen(
        authViewModel = authViewModel,
        onLogoutClick = {
          authViewModel.logout()
          navController.navigate(AppRoutes.LOGIN) { 
            popUpTo(AppRoutes.LOGIN) { inclusive = true } 
          }
        },
        onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) },
        onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) },
        onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) },
        onChatClick = { navController.navigate(AppRoutes.OPERATOR_LIST) }
      )
    }
    composable(AppRoutes.OPERATOR_HOME) {
      OperatorHomeScreen(
        viewModel = customerViewModel,
        onCustomerClick = { navController.navigate("${AppRoutes.CHAT}/${currentUser?.id}/${it.id}") },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onLogoutClick = {
          authViewModel.logout()
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
      val currentSenderId = currentUser?.id
      val currentUserRole = currentUser?.role

      if (operator != null && user != null && currentSenderId != null && currentUserRole != null) {
        ChatScreen(
          viewModel = chatViewModel,
          operator = operator,
          user = user,
          currentSenderId = currentSenderId,
          currentUserRole = currentUserRole
        )
      }
    }
    composable(AppRoutes.OPERATOR_LIST) {
      OperatorListScreen(
        usersViewModel = usersViewModel,
        authViewModel = authViewModel,
        onOperatorClick = { operator ->
          navController.navigate("${AppRoutes.CHAT}/${operator.id}/${currentUser?.id}")
        },
        navController = navController
      )
    }
    composable(AppRoutes.INVITES) {
      InvitesScreen(
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } }
      )
    }
    composable(AppRoutes.PROMOTIONS) {
      PromotionsScreen(
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } }
      )
    }
    composable(AppRoutes.CAMPAIGNS) {
      CampaignsScreen(
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } }
      )
    }
    composable(AppRoutes.BANNERS) {
      BannersScreen(
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } }
      )
    }
    composable(AppRoutes.EVENTS_CENTER) {
        EventsCenterScreen(navController = navController, onLogoutClick = { navController.navigate(AppRoutes.LOGIN) }, onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) }, onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) })
    }
    composable(AppRoutes.BUSINESS_CLUB) {
        BusinessClubScreen(navController = navController, onLogoutClick = { navController.navigate(AppRoutes.LOGIN) }, onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) }, onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) })
    }
    composable(AppRoutes.SHERATON_HOTEL) {
        SheratonHotelScreen(navController = navController, onLogoutClick = { navController.navigate(AppRoutes.LOGIN) }, onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) }, onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) })
    }
    composable(AppRoutes.SEGMENT_CHATS) { SegmentChatsScreen(navController = navController) }
    composable(
      route = "${AppRoutes.GROUP_CHAT}/{segment}",
      arguments = listOf(navArgument("segment") { type = NavType.StringType })
    ) { backStackEntry ->
      val segment = backStackEntry.arguments?.getString("segment")
      val chatViewModel: ChatViewModel = viewModel()
      val currentSenderId = currentUser?.id
      if (segment != null && currentSenderId != null) {
        GroupChatScreen(viewModel = chatViewModel, segment = segment, currentSenderId = currentSenderId)
      }
    }
    composable(
      route = "${AppRoutes.GROUP_CHAT_READONLY}/{segment}",
      arguments = listOf(navArgument("segment") { type = NavType.StringType })
    ) { backStackEntry ->
      val segment = backStackEntry.arguments?.getString("segment")
      val chatViewModel: ChatViewModel = viewModel()
      val currentSenderId = currentUser?.id
      if (segment != null && currentSenderId != null) {
        GroupChatReadOnlyScreen(viewModel = chatViewModel, segment = segment, currentSenderId = currentSenderId)
      }
    }
  }
}
