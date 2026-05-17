package br.com.savedra.challengecrm.data.repository

import android.util.Log
import br.com.savedra.challengecrm.data.api.AuthApi
import br.com.savedra.challengecrm.data.api.AuthenticationRequest
import br.com.savedra.challengecrm.data.api.CustomerApi
import br.com.savedra.challengecrm.data.api.RegisterRequest
import br.com.savedra.challengecrm.data.local.TokenManager
import br.com.savedra.challengecrm.model.User
import kotlinx.coroutines.tasks.await

/**
 * Repositório central para autenticação e gestão de usuários.
 */
class AuthRepository(
    private val authApi: AuthApi,
    private val customerApi: CustomerApi, // Adicionado para buscar lista de usuários
    private val tokenManager: TokenManager
) {
  /**
   * Realiza login e armazena o token JWT.
   */
  suspend fun login(email: String, password: String) {
    Log.d("AuthRepo", "Tentando login para: $email")
    val response = authApi.authenticate(AuthenticationRequest(email, password))
    if (response.isSuccessful) {
        Log.d("AuthRepo", "Login bem-sucedido via API")
        response.body()?.token?.let { token ->
            tokenManager.saveToken(token)
        }
    } else {
        val errorBody = response.errorBody()?.string()
        Log.e("AuthRepo", "FALHA NO LOGIN: Código ${response.code()}, Erro: $errorBody")
        throw Exception("Falha na autenticação: ${errorBody ?: response.message()}")
    }
  }

  /**
   * Realiza o registro de um novo usuário.
   */
  suspend fun register(
    email: String,
    password: String,
    name: String,
    company: String,
    role: String,
    segment: String,
    gender: String,
    phone: String,
    category: String
  ) {
    Log.d("AuthRepo", "Tentando registro para: $email")
    val request = RegisterRequest(name, email, password, role, company, segment, gender, phone, category)
    val response = authApi.register(request)
    if (response.isSuccessful) {
        Log.d("AuthRepo", "Registro bem-sucedido via API")
        response.body()?.token?.let { token ->
            tokenManager.saveToken(token)
        }
    } else {
        val errorBody = response.errorBody()?.string()
        Log.e("AuthRepo", "FALHA NO REGISTRO: Código ${response.code()}, Erro: $errorBody")
        throw Exception("Falha no cadastro: ${errorBody ?: response.message()}")
    }
  }

  /**
   * Retorna os dados do usuário atualmente logado.
   */
  suspend fun getCurrentUserData(): User? {
    val token = tokenManager.getToken() ?: return null
    val response = authApi.getMe()
    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
  }

  /**
   * Retorna TODOS os usuários do sistema.
   * Resolvido bug do "Ghost Registration": agora busca do endpoint real /all.
   */
  suspend fun getUsers(): List<User> {
    val response = customerApi.getAllUsers()
    return if (response.isSuccessful) {
        response.body() ?: emptyList()
    } else {
        Log.e("AuthRepo", "Erro ao buscar todos os usuários: ${response.code()}")
        emptyList()
    }
  }

  /**
   * Retorna apenas os usuários com cargo de OPERATOR.
   */
  suspend fun getOperators(): List<User> {
    val allUsers = getUsers()
    return allUsers.filter { it.role == "OPERATOR" || it.role == "Operador" }
  }

  fun logout() {
    tokenManager.clearToken()
  }
}