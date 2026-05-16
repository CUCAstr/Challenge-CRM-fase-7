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
    val response = authApi.authenticate(AuthenticationRequest(email, password))
    if (response.isSuccessful) {
        response.body()?.token?.let { token ->
            tokenManager.saveToken(token)
            // Still sync with Firebase for now if needed, or remove
            // auth.signInWithEmailAndPassword(email, password).await()
        }
    } else {
        throw Exception("Falha na autenticação: ${response.message()}")
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
    val request = RegisterRequest(name, email, password, role)
    val response = authApi.register(request)
    if (response.isSuccessful) {
        response.body()?.token?.let { token ->
            tokenManager.saveToken(token)
        }
    } else {
        throw Exception("Falha no cadastro: ${response.message()}")
    }
  }

  suspend fun getUserRole(uid: String): String? {
    val document = firestore.collection("users").document(uid).get().await()
    return document.getString("role")
  }

  suspend fun getCurrentUserData(): User? {
    val firebaseUser = auth.currentUser ?: return null
    val document = firestore.collection("users").document(firebaseUser.uid).get().await()
    return document.let {
        User(
            id = it.id,
            name = it.getString("name") ?: "",
            company = it.getString("company") ?: "",
            email = it.getString("email") ?: "",
            role = it.getString("role") ?: "",
            segment = it.getString("segment") ?: "",
            score = (it.getLong("score") ?: 0).toInt(),
            status = it.getString("status") ?: "",
            memberSince = it.getDate("memberSince"),
            notes = it.getString("notes") ?: "",
            gender = it.getString("gender") ?: "",
            phone = it.getString("phone") ?: "",
            category = it.getString("category") ?: ""
        )
    }
  }

  suspend fun getUsers(): List<User> {
    val snapshot = firestore.collection("users").get().await()
    return snapshot.documents.map { document ->
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