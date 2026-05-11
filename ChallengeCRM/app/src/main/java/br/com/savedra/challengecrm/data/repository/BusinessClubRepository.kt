package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.BusinessClub
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BusinessClubRepository(
  private val firestore: FirebaseFirestore
) {
  suspend fun saveBusinessClub(businessClub: BusinessClub) {
    firestore.collection("businessClub").add(businessClub).await()
  }
}
