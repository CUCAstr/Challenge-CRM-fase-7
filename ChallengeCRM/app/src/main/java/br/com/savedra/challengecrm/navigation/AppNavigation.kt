package br.com.savedra.challengecrm.navigation

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.com.savedra.challengecrm.ui.theme.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.savedra.challengecrm.ui.view.*
import br.com.savedra.challengecrm.viewmodel.*

class SafeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            try {
                val constructor = modelClass.getConstructor(Application::class.java)
                constructor.newInstance(application)
            } catch (e: NoSuchMethodException) {
                modelClass.getDeclaredConstructor().newInstance()
            }
        } catch (e: Exception) {
            Log.e("SafeFactory", "ERRO na criação de ${modelClass.simpleName}", e)
            throw e
        }
    }
}

@Composable
fun AppNavigation() {
  val context = LocalContext.current
  val application = context.applicationContext as Application
  val factory = remember { SafeViewModelFactory(application) }

  val navController = rememberNavController()

  val authViewModel: AuthViewModel = viewModel(factory = factory)
  val customerViewModel: OperatorViewModel = viewModel(factory = factory)
  val usersViewModel: UsersViewModel = viewModel(factory = factory)
  val bannerViewModel: BannerViewModel = viewModel(factory = factory)
  val campaignViewModel: CampaignViewModel = viewModel(factory = factory)
  val inviteViewModel: InviteViewModel = viewModel(factory = factory)
  val promotionViewModel: PromotionViewModel = viewModel(factory = factory)
  val chatViewModel: ChatViewModel = viewModel(factory = factory)

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
        navController = navController,
        authViewModel = authViewModel,
        bannerViewModel = bannerViewModel,
        campaignViewModel = campaignViewModel,
        onLogoutClick = {
          authViewModel.logout()
          navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.CLIENT_HOME) { inclusive = true } }
        },
        onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) },
        onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) },
        onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) },
        onChatClick = { 
            usersViewModel.loadUsers()
            navController.navigate(AppRoutes.OPERATOR_LIST) 
        }
      )
    }
    composable(AppRoutes.OPERATOR_HOME) {
      OperatorHomeScreen(
        navController = navController,
        viewModel = customerViewModel,
        onCustomerClick = { 
            usersViewModel.loadUsers()
            navController.navigate("${AppRoutes.CHAT}/${currentUser?.id}/${it.id}") 
        },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onLogoutClick = {
          authViewModel.logout()
          navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } }
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
          currentUserRole = currentUserRole,
          onBackClick = { navController.popBackStack() }
        )
      } else {
          LaunchedEffect(users, operatorId, userId) {
              if (users.isEmpty() || operator == null || user == null) {
                usersViewModel.loadUsers()
              }
          }
          Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
              Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                  CircularProgressIndicator(color = indigo500)
                  Spacer(Modifier.height(16.dp))
                  Text("Carregando conversa...", color = slate600)
              }
          }
      }
    }
    composable(AppRoutes.OPERATOR_LIST) {
      OperatorListScreen(
        navController = navController,
        authViewModel = authViewModel,
        usersViewModel = usersViewModel,
        onOperatorClick = { operator ->
          navController.navigate("${AppRoutes.CHAT}/${operator.id}/${currentUser?.id}")
        },
        onBackClick = { navController.popBackStack() }
      )
    }
    composable(AppRoutes.INVITES) {
      InvitesScreen(
        navController = navController,
        viewModel = inviteViewModel,
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { 
            authViewModel.logout()
            navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } 
        },
        onBackClick = { navController.popBackStack() }
      )
    }
    composable(AppRoutes.PROMOTIONS) {
      PromotionsScreen(
        navController = navController,
        viewModel = promotionViewModel,
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { 
            authViewModel.logout()
            navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } 
        },
        onBackClick = { navController.popBackStack() }
      )
    }
    composable(AppRoutes.CAMPAIGNS) {
      CampaignsScreen(
        navController = navController,
        viewModel = campaignViewModel,
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { 
            authViewModel.logout()
            navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } 
        },
        onBackClick = { navController.popBackStack() }
      )
    }
    composable(AppRoutes.BANNERS) {
      BannersScreen(
        navController = navController,
        viewModel = bannerViewModel,
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onChatsClick = { navController.navigate(AppRoutes.SEGMENT_CHATS) },
        onLogoutClick = { 
            authViewModel.logout()
            navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } } 
        },
        onBackClick = { navController.popBackStack() }
      )
    }
    composable(AppRoutes.EVENTS_CENTER) {
        EventsCenterScreen(
            navController = navController, 
            onLogoutClick = { authViewModel.logout(); navController.navigate(AppRoutes.LOGIN) }, 
            onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, 
            onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) }, 
            onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) }
        )
    }
    composable(AppRoutes.BUSINESS_CLUB) {
        BusinessClubScreen(
            navController = navController, 
            onLogoutClick = { authViewModel.logout(); navController.navigate(AppRoutes.LOGIN) }, 
            onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, 
            onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) }, 
            onSheratonHotelClick = { navController.navigate(AppRoutes.SHERATON_HOTEL) }
        )
    }
    composable(AppRoutes.SHERATON_HOTEL) {
        SheratonHotelScreen(
            navController = navController, 
            onLogoutClick = { authViewModel.logout(); navController.navigate(AppRoutes.LOGIN) }, 
            onInboxClick = { navController.navigate(AppRoutes.CLIENT_HOME) }, 
            onBusinessClubClick = { navController.navigate(AppRoutes.BUSINESS_CLUB) }, 
            onEventsCenterClick = { navController.navigate(AppRoutes.EVENTS_CENTER) }
        )
    }
    composable(AppRoutes.SEGMENT_CHATS) { 
      SegmentChatsScreen(
        navController = navController,
        onClientsClick = { navController.navigate(AppRoutes.OPERATOR_HOME) { popUpTo(AppRoutes.OPERATOR_HOME) { inclusive = true } } },
        onInvitesClick = { navController.navigate(AppRoutes.INVITES) },
        onPromotionsClick = { navController.navigate(AppRoutes.PROMOTIONS) },
        onCampaignsClick = { navController.navigate(AppRoutes.CAMPAIGNS) },
        onBannersClick = { navController.navigate(AppRoutes.BANNERS) },
        onLogoutClick = {
          authViewModel.logout()
          navController.navigate(AppRoutes.LOGIN) { popUpTo(AppRoutes.LOGIN) { inclusive = true } }
        }
      ) 
    }
    composable(
      route = "${AppRoutes.GROUP_CHAT}/{segment}",
      arguments = listOf(navArgument("segment") { type = NavType.StringType })
    ) { backStackEntry ->
      val segment = backStackEntry.arguments?.getString("segment")
      val currentSenderId = currentUser?.id
      if (segment != null && currentSenderId != null) {
        GroupChatScreen(
            viewModel = chatViewModel, 
            segment = segment, 
            currentSenderId = currentSenderId,
            onBackClick = { navController.popBackStack() }
        )
      }
    }
    composable(
      route = "${AppRoutes.GROUP_CHAT_READONLY}/{segment}",
      arguments = listOf(navArgument("segment") { type = NavType.StringType })
    ) { backStackEntry ->
      val segment = backStackEntry.arguments?.getString("segment")
      val currentSenderId = currentUser?.id
      if (segment != null && currentSenderId != null) {
        GroupChatReadOnlyScreen(
            viewModel = chatViewModel, 
            segment = segment, 
            currentSenderId = currentSenderId,
            onBackClick = { navController.popBackStack() }
        )
      }
    }
  }
}
