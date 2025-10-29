package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.EventsCenter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveEvent(event: EventsCenter) {
        firestore.collection("eventsCenter").add(event).await()
    }
}