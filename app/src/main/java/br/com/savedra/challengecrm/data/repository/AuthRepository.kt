package br.com.savedra.challengecrm.data.repository

import android.util.Log
import br.com.savedra.challengecrm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
  private val auth: FirebaseAuth,
  private val firestore: FirebaseFirestore
) {
  fun getCurrentUser() = auth.currentUser

  suspend fun login(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).await()
  }

  suspend fun register(email: String, password: String, name: String, role: String, estado: String, segment: String) {
    val authResult = auth.createUserWithEmailAndPassword(email, password).await()
    val firebaseUser = authResult.user
      ?: throw Exception("Usuário não encontrado após o cadasdtro.")

    val profileUpdates = userProfileChangeRequest {
      displayName = name
    }

    firebaseUser.updateProfile(profileUpdates).await()

    val userData = hashMapOf(
      "uid" to firebaseUser.uid,
      "email" to email,
      "name" to name,
      "role" to role,
      "estado" to estado,
      "segment" to segment,
      "memberSince" to FieldValue.serverTimestamp(),
      "notes" to ""
    )

    if (role == "Cliente") {
      userData["score"] = 0
      userData["status"] = "Ativo"
    }

    Log.d("AuthRepository", "userData: $userData")

    firestore.collection("users").document(firebaseUser.uid)
      .set(userData)
      .await()
  }

  suspend fun getUserRole(uid: String): String? {
    val document = firestore.collection("users").document(uid).get().await()
    return document.getString("role")
  }

  suspend fun getUsers(): List<User> {
    val snapshot = firestore.collection("users").get().await()
    return snapshot.documents.map { document ->
      User(
        id = document.id,
        name = document.getString("name") ?: "",
        email = document.getString("email") ?: "",
        role = document.getString("role") ?: "",
        estado = document.getString("estado") ?: "",
        segment = document.getString("segment") ?: "",
        score = (document.getLong("score") ?: 0).toInt(),
        status = document.getString("status") ?: "",
        notes = document.getString("notes") ?: ""
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