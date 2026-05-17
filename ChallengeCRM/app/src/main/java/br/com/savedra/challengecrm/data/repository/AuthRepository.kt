package br.com.savedra.challengecrm.data.repository

import android.util.Log
import br.com.savedra.challengecrm.data.api.AuthApi
import br.com.savedra.challengecrm.data.api.AuthenticationRequest
import br.com.savedra.challengecrm.data.api.RegisterRequest
import br.com.savedra.challengecrm.data.local.TokenManager
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
  fun getCurrentUser() = auth.currentUser

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

  suspend fun getCurrentUserData(): User? {
    val token = tokenManager.getToken() ?: return null
    val response = authApi.getMe()
    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
  }

  suspend fun getUsers(): List<User> {
    // For now, if we don't have a specific getUsers endpoint, 
    // we might need to add one or use the CustomerRepository if it fits.
    // But since this is AuthRepository, let's assume we want all users.
    return emptyList() // TODO: Implement REST endpoint for this
  }

  suspend fun getOperators(): List<User> {
    // Tenta por role em PT/EN. Se vazio, faz fallback trazendo todos e filtrando != Cliente
    val ptEnRoles = listOf("Operador", "Operator")
    val query = firestore.collection("users")
      .whereIn("role", ptEnRoles)
    val snapshot = try {
      query.get().await()
    } catch (_: Exception) {
      null
    }

    val docs = snapshot?.documents?.takeIf { it.isNotEmpty() } ?: run {
      val all = firestore.collection("users").get().await()
      all.documents.filter { (it.getString("role") ?: "") != "Cliente" }
    }

    return docs.map { document ->
      User(
        id = document.id,
        name = document.getString("name") ?: "",
        company = document.getString("company") ?: "",
        email = document.getString("email") ?: "",
        role = document.getString("role") ?: "",
        segment = document.getString("segment") ?: "",
        score = (document.getLong("score") ?: 0).toInt(),
        status = document.getString("status") ?: "",
        memberSince = document.getTimestamp("memberSince")?.toDate(),
        notes = document.getString("notes") ?: "",
        gender = document.getString("gender") ?: "",
        phone = document.getString("phone") ?: "",
        category = document.getString("category") ?: ""
      )
    }
  }

  suspend fun updateUserNotes(userId: String, notes: String) {
    firestore.collection("users").document(userId)
      .update("notes", notes)
      .await()
  }

  fun logout() {
    auth.signOut()
  }
}