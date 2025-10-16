package br.com.savedra.challengecrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.savedra.challengecrm.navigation.AppNavigation
import br.com.savedra.challengecrm.ui.theme.ChallengeCRMTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ChallengeCRMTheme {
        AppNavigation()
      }
    }
  }
}