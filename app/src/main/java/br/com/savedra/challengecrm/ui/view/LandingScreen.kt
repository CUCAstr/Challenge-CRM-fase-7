package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.savedra.challengecrm.R

import br.com.savedra.challengecrm.ui.theme.purple500
import br.com.savedra.challengecrm.ui.theme.slate50
import br.com.savedra.challengecrm.ui.theme.slate500
import br.com.savedra.challengecrm.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onAccessSystemClick: () -> Unit
) {
    Scaffold(
        containerColor = slate50,
        topBar = {
            TopAppBar(
                title = { Text("WTC Connect") },
                actions = {
                    Button(onClick = onAccessSystemClick) {
                        Text("Acessar sistema")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = white
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SobreNosSection()
            Spacer(modifier = Modifier.height(32.dp))
            NossosPilaresSection()
            Spacer(modifier = Modifier.height(32.dp))
            ClientesDestaqueSection()
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SobreNosSection() {
    Column {
        Text(
            "Sobre nós",
            style = MaterialTheme.typography.headlineMedium,
            color = purple500,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Criado há mais de 50 anos pelos irmãos Rockefellers\", o World Trade Center Association (WTC) é a maior associação de empresários e executivos do mundo, presente em mais de 300 cidades e 90 países. Integrante dessa rede desde 2007, o WTC Business Club São Paulo é um clube de negócios focado na prosperidade empresarial. Conectando empresas e executivos a oportunidade estratégicas no Brasil e no mundo promovendo parcerias e um ambiente favorável para o crescimento em um cenário dinâmico e globalizado.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NossosPilaresSection() {
    Column {
        Text(
            "Nossos pilares",
            style = MaterialTheme.typography.headlineMedium,
            color = purple500,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        PilarItem("Networking de alto nível")
        PilarItem("Geração de oportunidade de negócios")
        PilarItem("Conteúdo de mercado")
        PilarItem("Internacionalização")
    }
}

@Composable
fun PilarItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ClientesDestaqueSection() {
    Column {
        Text(
            "Clientes em destaque",
            style = MaterialTheme.typography.headlineMedium,
            color = purple500,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "+150 empresas",
            color = slate500,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Acer", fontWeight = FontWeight.Bold)
            Text("BIC", fontWeight = FontWeight.Bold)
            Text("ESPM", fontWeight = FontWeight.Bold)
            Text("Lenovo", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Sem Parar", fontWeight = FontWeight.Bold)
            Text("Enel", fontWeight = FontWeight.Bold)
            Text("Sabesp ", fontWeight = FontWeight.Bold)
            Text("etc...", fontWeight = FontWeight.Bold)
        }
    }
}