package br.com.savedra.challengecrm.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
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

  suspend fun register(email: String, password: String, name: String, role: String) {
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
      "role" to role
    )

    firestore.collection("users").document(firebaseUser.uid)
      .set(userData)
      .await()
  }

  suspend fun getUserRole(uid: String): String? {
    val document = firestore.collection("users").document(uid).get().await()
    return document.getString("role")
  }

  fun logout() {
    auth.signOut()
  }
}