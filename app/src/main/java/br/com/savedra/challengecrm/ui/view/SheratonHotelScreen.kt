package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import br.com.savedra.challengecrm.ui.theme.slate50

@Composable
fun SheratonHotelScreen(
    onLogoutClick: () -> Unit,
    onInboxClick: () -> Unit,
    onEventsCenterClick: () -> Unit,
    onBusinessClubClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(slate50)
    ) {
        Text(
            text = "Sheraton Hotel",
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
        BottomNavigationClient(
            onInboxClick = onInboxClick,
            onLogoutClick = onLogoutClick,
            isInboxActive = false,
            isEventsCenterActive = false,
            isBusinessClubActive = false,
            isSheratonHotelActive = true,
            modifier = Modifier.align(Alignment.BottomCenter),
            onEventsCenterClick = onEventsCenterClick,
            onBusinessClubClick = onBusinessClubClick,
            onSheratonHotelClick = { }
        )
    }
}