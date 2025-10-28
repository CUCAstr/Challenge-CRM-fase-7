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

  fun getBanners(onSuccess: (List<Banner>) -> Unit, onFailure: (Exception) -> Unit) {
    firestore.collection("banners")
      .get()
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
