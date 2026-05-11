package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.SheratonHotel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SheratonHotelRepository(
  private val firestore: FirebaseFirestore
) {
  suspend fun saveSheratonHotel(sheratonHotel: SheratonHotel) {
    firestore.collection("sheratonHotel").add(sheratonHotel).await()
  }
}