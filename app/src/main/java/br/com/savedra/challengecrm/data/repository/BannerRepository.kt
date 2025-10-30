package br.com.savedra.challengecrm.data.repository

import br.com.savedra.challengecrm.model.Banner
import com.google.firebase.firestore.FirebaseFirestore

class BannerRepository {
  private val firestore = FirebaseFirestore.getInstance()

  fun sendBanner(banner: Banner, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    firestore.collection("banners")
      .add(banner)
      .addOnSuccessListener {
        onSuccess()
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }

  fun getBanners(userSegment: String? = null, onSuccess: (List<Banner>) -> Unit, onFailure: (Exception) -> Unit) {
    val query = if (userSegment != null) {
        firestore.collection("banners").whereIn("segment", listOf(userSegment, "Todos"))
    } else {
        firestore.collection("banners")
    }
    query.get()
      .addOnSuccessListener { result ->
        val banners = result.map { document ->
          document.toObject(Banner::class.java)
        }
        onSuccess(banners)
      }
      .addOnFailureListener { e ->
        onFailure(e)
      }
  }
}
