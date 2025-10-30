package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun saveEvent(event: Event) {
        firestore.collection("eventsCenter").add(event).await()
    }

    suspend fun getEvents(): List<Event> {
        val snapshot = firestore.collection("eventsCenter").get().await()
        return snapshot.toObjects(Event::class.java)
    }
}