package br.com.savedra.challengecrm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.savedra.challengecrm.navigation.AppNavigation // CORREÇÃO: Novo local do navegador
import br.com.savedra.challengecrm.ui.theme.ChallengeCRMTheme

/**
 * Ponto de entrada principal do Aplicativo.
 */
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ChallengeCRMTheme(dynamicColor = false) {
        AppNavigation()
      }
    }
  }
}