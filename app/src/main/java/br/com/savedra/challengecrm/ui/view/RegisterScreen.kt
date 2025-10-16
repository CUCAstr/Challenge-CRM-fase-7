package br.com.savedra.challengecrm.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.savedra.challengecrm.navigation.AppRoutes
import br.com.savedra.challengecrm.ui.theme.ChallengeCRMTheme
import br.com.savedra.challengecrm.ui.theme.indigo500
import br.com.savedra.challengecrm.ui.theme.interFamily
import br.com.savedra.challengecrm.ui.theme.slate100
import br.com.savedra.challengecrm.ui.theme.slate200
import br.com.savedra.challengecrm.ui.theme.slate600
import br.com.savedra.challengecrm.ui.theme.slate800

@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier) {
  var completeName by rememberSaveable { mutableStateOf("") }
  var document by rememberSaveable { mutableStateOf("") }
  var email by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var passwordVisible by rememberSaveable { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = slate100)
  ) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Bem-vindo ao CRM",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(bottom = 6.dp)
      )

      Text(
        text = "Crie sua conta para continuar",
        style = TextStyle(
          fontFamily = interFamily,
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(bottom = 24.dp)
      )

      OutlinedTextField(
        value = completeName,
        onValueChange = { completeName = it },
        label = { Text("Nome Completo") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier
          .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = document,
        onValueChange = { document = it },
        label = { Text("CPF") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
          .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
          .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200
        ),
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
          val image = if (passwordVisible)
            Icons.Filled.Visibility
          else Icons.Filled.VisibilityOff

          val description = if (passwordVisible) "Hide password" else "Show password"

          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(imageVector = image, description)
          }
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = indigo500,
          unfocusedBorderColor = slate200
        ),
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = { navController.navigate(AppRoutes.LOGIN) },
        modifier = Modifier
          .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
          containerColor = slate600,
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text ="Cadastrar como cliente",
          style = TextStyle(
            fontFamily = interFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
          )
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
  ChallengeCRMTheme {
    RegisterScreen(navController = rememberNavController())
  }
}
