package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Banner
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BannerRepository {
  private val firestore = FirebaseFirestore.getInstance()

  suspend fun sendBanner(banner: Banner) {
    firestore.collection("banners").add(banner).await()
  }

  suspend fun getBanners(userSegment: String? = null): List<Banner> {
    val query = if (userSegment != null) {
        firestore.collection("banners").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("banners")
    }
    val result = query.get().await()
    return result.map { document ->
        document.toObject(Banner::class.java)
    }
  }
}
